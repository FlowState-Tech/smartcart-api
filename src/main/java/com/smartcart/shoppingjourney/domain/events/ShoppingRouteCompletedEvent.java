package com.smartcart.shoppingjourney.domain.events;

public record ShoppingRouteCompletedEvent(String routeId, Long buyerId, Long storeId, Long listId) {}
