package com.smartcart.shoppingjourney.interfaces.rest.resources;

public record RouteStopResponse(Long storeId, String storeName, double latitude, double longitude, int sequence) {}
