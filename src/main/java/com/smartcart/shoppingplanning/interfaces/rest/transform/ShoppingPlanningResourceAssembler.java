package com.smartcart.shoppingplanning.interfaces.rest.transform;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingPreferences;
import com.smartcart.shoppingplanning.domain.model.valueobjects.PriceComparisonResult;
import com.smartcart.shoppingplanning.interfaces.rest.resources.PreferencesResponse;
import com.smartcart.shoppingplanning.interfaces.rest.resources.PriceComparisonResponse;
import com.smartcart.shoppingplanning.interfaces.rest.resources.ShoppingListResponse;

public final class ShoppingPlanningResourceAssembler {

    private ShoppingPlanningResourceAssembler() {}

    public static ShoppingListResponse toListResponse(ShoppingList list) {
        var items = list.getItems().stream()
                .map(i -> new ShoppingListResponse.ItemResponse(
                        i.getId(), i.getSku(), i.getProductName(), i.getQuantity(), i.getUnit()))
                .toList();
        return new ShoppingListResponse(list.getId(), list.getBuyerId(), list.getName(), items);
    }

    public static PreferencesResponse toPrefsResponse(ShoppingPreferences p) {
        var budget = p.getBudget();
        return new PreferencesResponse(p.getBuyerId(), p.getFamilyBasketSkus(), p.getPreferredStoreIds(),
                budget != null ? budget.getAmount() : null,
                budget != null ? budget.getCurrency() : null,
                p.getResidenceLat(), p.getResidenceLng());
    }

    public static PriceComparisonResponse toComparison(PriceComparisonResult r) {
        return new PriceComparisonResponse(r.storeId(), r.storeName(), r.totalCost(), r.currency(),
                r.itemsFound(), r.itemsMissing(), r.savingsVsWorst(), r.savingsPercent(),
                r.withinBudget(), r.budgetLimit());
    }
}
