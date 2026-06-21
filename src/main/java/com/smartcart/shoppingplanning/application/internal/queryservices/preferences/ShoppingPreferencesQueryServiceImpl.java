package com.smartcart.shoppingplanning.application.internal.queryservices.preferences;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingPreferences;
import com.smartcart.shoppingplanning.domain.model.queries.GetPreferencesQuery;
import com.smartcart.shoppingplanning.domain.services.preferences.ShoppingPreferencesQueryService;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingPreferencesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ShoppingPreferencesQueryServiceImpl implements ShoppingPreferencesQueryService {

    private final ShoppingPreferencesRepository preferencesRepository;

    public ShoppingPreferencesQueryServiceImpl(ShoppingPreferencesRepository preferencesRepository) {
        this.preferencesRepository = preferencesRepository;
    }

    @Override
    public Optional<ShoppingPreferences> handle(GetPreferencesQuery query) {
        return preferencesRepository.findByBuyerId(query.buyerId());
    }
}
