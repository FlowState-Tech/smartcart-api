package com.smartcart.shoppingplanning.application.internal.queryservices.pricecomparison;

import com.smartcart.shared.domain.exceptions.ResourceNotFoundException;
import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingPreferences;
import com.smartcart.shoppingplanning.domain.model.queries.GetPriceProjectionsQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetSubstitutesQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetTotalCostQuery;
import com.smartcart.shoppingplanning.domain.model.queries.LookupBarcodeQuery;
import com.smartcart.shoppingplanning.domain.model.queries.VerifyStockQuery;
import com.smartcart.shoppingplanning.domain.model.valueobjects.BarcodeLookupResult;
import com.smartcart.shoppingplanning.domain.model.valueobjects.PriceComparisonResult;
import com.smartcart.shoppingplanning.domain.events.ProductStockOutDetectedEvent;
import com.smartcart.shoppingplanning.domain.events.SubstituteProductSuggestedEvent;
import com.smartcart.shoppingplanning.domain.model.valueobjects.SubstituteSuggestion;
import com.smartcart.shoppingplanning.domain.services.ComparisonEngineService;
import com.smartcart.shoppingplanning.domain.services.pricecomparison.PriceComparisonQueryService;
import com.smartcart.shoppingplanning.domain.services.pricecomparison.SubstituteSuggestionService;
import com.smartcart.shoppingplanning.infrastructure.acl.StoreCatalogACL;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingListRepository;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingPreferencesRepository;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PriceComparisonQueryServiceImpl implements PriceComparisonQueryService {

    private final ShoppingListRepository listRepository;
    private final ShoppingPreferencesRepository preferencesRepository;
    private final StoreRepository storeRepository;
    private final ComparisonEngineService comparisonEngine;
    private final StoreCatalogACL catalogACL;
    private final SubstituteSuggestionService substituteService;
    private final ApplicationEventPublisher eventPublisher;

    public PriceComparisonQueryServiceImpl(ShoppingListRepository listRepository,
                                           ShoppingPreferencesRepository preferencesRepository,
                                           StoreRepository storeRepository,
                                           ComparisonEngineService comparisonEngine,
                                           StoreCatalogACL catalogACL,
                                           SubstituteSuggestionService substituteService,
                                           ApplicationEventPublisher eventPublisher) {
        this.listRepository = listRepository;
        this.preferencesRepository = preferencesRepository;
        this.storeRepository = storeRepository;
        this.comparisonEngine = comparisonEngine;
        this.catalogACL = catalogACL;
        this.substituteService = substituteService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<PriceComparisonResult> handle(GetPriceProjectionsQuery query) {
        var list = findList(query.listId());
        var storeIds = filterStores(resolveStoreIds(list.getBuyerId()), query.storeFormat());
        var budget = preferencesRepository.findByBuyerId(list.getBuyerId())
                .map(ShoppingPreferences::getBudget).orElse(null);
        return comparisonEngine.compare(list, storeIds, budget);
    }

    @Override
    public Optional<BigDecimal> handle(GetTotalCostQuery query) {
        return handle(new GetPriceProjectionsQuery(query.listId(), null)).stream()
                .findFirst()
                .map(PriceComparisonResult::totalCost);
    }

    @Override
    public List<String> handle(VerifyStockQuery query) {
        var list = findList(query.listId());
        var outOfStock = new ArrayList<String>();
        for (var item : list.getItems()) {
            if (!catalogACL.isInStock(query.storeId(), item.getSku())) {
                outOfStock.add(item.getSku());
                eventPublisher.publishEvent(new ProductStockOutDetectedEvent(
                        item.getSku(), query.storeId(), Instant.now()));
            }
        }
        return outOfStock;
    }

    @Override
    public Optional<SubstituteSuggestion> handle(GetSubstitutesQuery query) {
        return substituteService.suggestAlternative(query.storeId(), query.sku())
                .map(suggestion -> {
                    publishSubstituteSuggested(suggestion);
                    return suggestion;
                });
    }

    @Override
    public List<BarcodeLookupResult> handle(LookupBarcodeQuery query) {
        return catalogACL.lookupBarcode(query.barcode());
    }

    @Override
    public List<SubstituteSuggestion> handleAllSubstitutes(VerifyStockQuery query) {
        return handle(query).stream()
                .map(sku -> substituteService.suggestAlternative(query.storeId(), sku).orElse(null))
                .filter(s -> s != null)
                .peek(this::publishSubstituteSuggested)
                .toList();
    }

    private void publishSubstituteSuggested(SubstituteSuggestion suggestion) {
        eventPublisher.publishEvent(new SubstituteProductSuggestedEvent(
                suggestion.originalSku(),
                suggestion.substituteSku(),
                suggestion.storeId(),
                suggestion.reason()));
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

    private List<Long> filterStores(List<Long> storeIds, String storeFormat) {
        if (storeFormat == null || storeFormat.isBlank()) return storeIds;
        var needle = storeFormat.toLowerCase(Locale.ROOT);
        return storeIds.stream()
                .filter(id -> catalogACL.storeName(id).toLowerCase(Locale.ROOT).contains(needle))
                .toList();
    }
}
