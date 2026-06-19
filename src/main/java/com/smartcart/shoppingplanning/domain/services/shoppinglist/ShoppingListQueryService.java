package com.smartcart.shoppingplanning.domain.services.shoppinglist;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.queries.GetShoppingListsByBuyerQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetShoppingListQuery;

import java.util.List;
import java.util.Optional;

public interface ShoppingListQueryService {
    Optional<ShoppingList> handle(GetShoppingListQuery query);
    List<ShoppingList> handle(GetShoppingListsByBuyerQuery query);
}
