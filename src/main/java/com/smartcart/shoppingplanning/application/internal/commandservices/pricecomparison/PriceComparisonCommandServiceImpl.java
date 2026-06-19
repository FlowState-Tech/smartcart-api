package com.smartcart.shoppingplanning.application.internal.commandservices.pricecomparison;

import com.smartcart.shared.domain.exceptions.ResourceNotFoundException;
import com.smartcart.shared.infrastructure.events.BasketComparedIntegrationEvent;
import com.smartcart.shoppingplanning.domain.events.BasketTotalCostProjectedEvent;
import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingPreferences;
import com.smartcart.shoppingplanning.domain.model.commands.CompareBasketCommand;
import com.smartcart.shoppingplanning.domain.model.valueobjects.CompareBasketResult;
import com.smartcart.shoppingplanning.domain.model.valueobjects.PriceComparisonResult;
import com.smartcart.shoppingplanning.domain.services.ComparisonEngineService;
import com.smartcart.shoppingplanning.domain.services.pricecomparison.PriceComparisonCommandService;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingListRepository;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingPreferencesRepository;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PriceComparisonCommandServiceImpl implements PriceComparisonCommandService {

    private static final int TOP_STORES_FOR_ROUTE = 3;

    private final ShoppingListRepository listRepository;
    private final ShoppingPreferencesRepository preferencesRepository;
    private final StoreRepository storeRepository;
    private final ComparisonEngineService comparisonEngine;
    private final ApplicationEventPublisher eventPublisher;

    public PriceComparisonCommandServiceImpl(ShoppingListRepository listRepository,
                                             ShoppingPreferencesRepository preferencesRepository,
                                             StoreRepository storeRepository,
                                             ComparisonEngineService comparisonEngine,
                                             ApplicationEventPublisher eventPublisher) {
        this.listRepository = listRepository;
        this.preferencesRepository = preferencesRepository;
        this.storeRepository = storeRepository;
        this.comparisonEngine = comparisonEngine;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CompareBasketResult handle(CompareBasketCommand command) {
        var list = findList(command.listId());
        var prefs = preferencesRepository.findByBuyerId(list.getBuyerId()).orElse(null);
        var budget = prefs != null ? prefs.getBudget() : null;
        var results = comparisonEngine.compare(list, resolveStoreIds(list.getBuyerId()), budget);

        if (results.isEmpty()) {
            return new CompareBasketResult(results, false, null);
        }

        var rankedStoreIds = rankedStores(results);
        var best = results.getFirst();

        eventPublisher.publishEvent(new BasketTotalCostProjectedEvent(
                list.getBuyerId(), list.getId(), best.storeId(), best.totalCost(), rankedStoreIds));

        eventPublisher.publishEvent(new BasketComparedIntegrationEvent(
                list.getBuyerId(), list.getId(), best.storeId(), best.totalCost(), rankedStoreIds));

        var journeyEndpoint = "/api/v1/journey/routes?buyerId=" + list.getBuyerId()
                + "&listId=" + list.getId();
        return new CompareBasketResult(results, true, journeyEndpoint);
    }

    private List<Long> rankedStores(List<PriceComparisonResult> results) {
        var ranked = results.stream()
                .filter(PriceComparisonResult::withinBudget)
                .map(PriceComparisonResult::storeId)
                .limit(TOP_STORES_FOR_ROUTE)
                .toList();
        return ranked.isEmpty()
                ? results.stream().map(PriceComparisonResult::storeId).limit(TOP_STORES_FOR_ROUTE).toList()
                : ranked;
    }

    private ShoppingList findList(Long listId) {
        return listRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingList", listId));
    }

    private List<Long> resolveStoreIds(Long buyerId) {
        var preferred = preferencesRepository.findByBuyerId(buyerId)
                .map(ShoppingPreferences::getPreferredStoreIds).orElse(List.of());
        if (!preferred.isEmpty()) return preferred;
        return storeRepository.findAll().stream().map(s -> s.getId()).toList();
    }
}
