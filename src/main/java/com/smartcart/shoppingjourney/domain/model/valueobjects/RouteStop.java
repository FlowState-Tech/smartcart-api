package com.smartcart.shoppingjourney.domain.model.valueobjects;

public record RouteStop(Long storeId, String storeName, double latitude, double longitude, int sequence) {}
