package com.smartcart.shoppingplanning.domain.services.preferences;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingPreferences;
import com.smartcart.shoppingplanning.domain.model.queries.GetPreferencesQuery;

import java.util.Optional;

public interface ShoppingPreferencesQueryService {
    Optional<ShoppingPreferences> handle(GetPreferencesQuery query);
}
