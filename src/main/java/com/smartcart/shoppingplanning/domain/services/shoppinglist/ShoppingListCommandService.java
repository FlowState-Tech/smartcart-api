package com.smartcart.shoppingplanning.domain.services.shoppinglist;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.commands.AddItemToListCommand;
import com.smartcart.shoppingplanning.domain.model.commands.ApplyFamilyBasketCommand;
import com.smartcart.shoppingplanning.domain.model.commands.CreateShoppingListCommand;
import com.smartcart.shoppingplanning.domain.model.commands.RemoveItemFromListCommand;

public interface ShoppingListCommandService {
    ShoppingList handle(CreateShoppingListCommand command);
    ShoppingList handle(AddItemToListCommand command);
    ShoppingList handle(RemoveItemFromListCommand command);
    ShoppingList handle(ApplyFamilyBasketCommand command);
}
