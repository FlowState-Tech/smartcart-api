package com.smartcart.shoppingjourney.domain.events;

public record StoreVisitConfirmedEvent(String routeId, Long storeId, int stopIndex) {}
