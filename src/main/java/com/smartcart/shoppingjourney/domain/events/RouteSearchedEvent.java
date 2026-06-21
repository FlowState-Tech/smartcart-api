package com.smartcart.shoppingjourney.domain.events;

public record RouteSearchedEvent(String routeId, Double originLat, Double originLng, Long destinationStoreId) {}
