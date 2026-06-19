package com.smartcart.shoppingjourney.domain.events;

public record NavigationToStoreStartedEvent(String routeId, Long buyerId) {}
