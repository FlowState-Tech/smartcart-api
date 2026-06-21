package com.smartcart.shoppingplanning.domain.events;

public record ProductAddedToBasketEvent(Long listId, String sku, java.math.BigDecimal quantity) {}
