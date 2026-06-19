package com.smartcart.shoppingplanning.application.internal.commandservices.preferences;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingPreferences;
import com.smartcart.shoppingplanning.domain.model.commands.ConfigureFamilyBasketCommand;
import com.smartcart.shoppingplanning.domain.model.commands.DefineBudgetCommand;
import com.smartcart.shoppingplanning.domain.model.commands.DefineResidencePreferenceCommand;
import com.smartcart.shoppingplanning.domain.model.commands.SelectPreferredStoresCommand;
import com.smartcart.shoppingplanning.domain.model.valueobjects.Budget;
import com.smartcart.shoppingplanning.domain.services.preferences.ShoppingPreferencesCommandService;
import com.smartcart.shoppingplanning.domain.events.MaxBudgetDefinedEvent;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingPreferencesRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingPreferencesCommandServiceImpl implements ShoppingPreferencesCommandService {

    private final ShoppingPreferencesRepository preferencesRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ShoppingPreferencesCommandServiceImpl(ShoppingPreferencesRepository preferencesRepository,
                                                 ApplicationEventPublisher eventPublisher) {
        this.preferencesRepository = preferencesRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ShoppingPreferences handle(ConfigureFamilyBasketCommand command) {
        var prefs = preferencesRepository.findByBuyerId(command.buyerId())
                .orElseGet(() -> new ShoppingPreferences(command.buyerId()));
        prefs.configureFamilyBasket(command.skus());
        return preferencesRepository.save(prefs);
    }

    @Override
    @Transactional
    public ShoppingPreferences handle(SelectPreferredStoresCommand command) {
        var prefs = preferencesRepository.findByBuyerId(command.buyerId())
                .orElseGet(() -> new ShoppingPreferences(command.buyerId()));
        prefs.selectPreferredStores(command.storeIds());
        return preferencesRepository.save(prefs);
    }

    @Override
    @Transactional
    public ShoppingPreferences handle(DefineBudgetCommand command) {
        var prefs = preferencesRepository.findByBuyerId(command.buyerId())
                .orElseGet(() -> new ShoppingPreferences(command.buyerId()));
        prefs.defineBudget(new Budget(command.amount(), command.currency()));
        var saved = preferencesRepository.save(prefs);
        eventPublisher.publishEvent(new MaxBudgetDefinedEvent(
                command.buyerId(), command.amount(), command.currency()));
        return saved;
    }

    @Override
    @Transactional
    public ShoppingPreferences handle(DefineResidencePreferenceCommand command) {
        var prefs = preferencesRepository.findByBuyerId(command.buyerId())
                .orElseGet(() -> new ShoppingPreferences(command.buyerId()));
        prefs.defineResidence(command.latitude(), command.longitude());
        return preferencesRepository.save(prefs);
    }
}
