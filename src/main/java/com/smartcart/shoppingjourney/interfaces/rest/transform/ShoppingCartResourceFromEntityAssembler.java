package com.smartcart.shoppingjourney.interfaces.rest.transform;

import com.smartcart.shoppingjourney.domain.model.entities.ShoppingCart;
import com.smartcart.shoppingjourney.interfaces.rest.resources.ShoppingCartResource;

public class ShoppingCartResourceFromEntityAssembler {
    public static ShoppingCartResource toResourceFromEntity(ShoppingCart entity) {
        return new ShoppingCartResource(
            entity.getId(), 
            entity.getCustomerName(), 
            entity.getTotalAmount(), 
            entity.getStatus()
        );
    }
}