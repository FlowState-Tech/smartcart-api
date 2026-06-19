package com.smartcart.shoppingplanning.domain.services.preferences;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingPreferences;
import com.smartcart.shoppingplanning.domain.model.commands.ConfigureFamilyBasketCommand;
import com.smartcart.shoppingplanning.domain.model.commands.DefineBudgetCommand;
import com.smartcart.shoppingplanning.domain.model.commands.DefineResidencePreferenceCommand;
import com.smartcart.shoppingplanning.domain.model.commands.SelectPreferredStoresCommand;

public interface ShoppingPreferencesCommandService {
    ShoppingPreferences handle(ConfigureFamilyBasketCommand command);
    ShoppingPreferences handle(SelectPreferredStoresCommand command);
    ShoppingPreferences handle(DefineBudgetCommand command);
    ShoppingPreferences handle(DefineResidencePreferenceCommand command);
}
