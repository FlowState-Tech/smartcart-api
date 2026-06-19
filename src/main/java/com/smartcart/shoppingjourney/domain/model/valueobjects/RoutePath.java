package com.smartcart.shoppingjourney.domain.model.valueobjects;

public record RoutePath(String encodedPolyline, int distanceMeters, int durationSeconds, String provider) {}
