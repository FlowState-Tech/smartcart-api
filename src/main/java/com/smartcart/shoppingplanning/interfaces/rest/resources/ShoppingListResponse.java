package com.smartcart.shoppingplanning.interfaces.rest.resources;
import java.math.BigDecimal;
import java.util.List;
public record ShoppingListResponse(Long id, Long buyerId, String name, List<ItemResponse> items) {
    public record ItemResponse(Long id, String sku, String productName, BigDecimal quantity, String unit) {}
}
