package com.smartcart.shoppingjourney.interfaces.rest.resources;

import java.util.List;

public record RouteResponse(
        String routeId,
        Long buyerId,
        Long listId,
        Double residenceLat,
        Double residenceLng,
        Long destinationStoreId,
        String status,
        String encodedPolyline,
        Integer distanceMeters,
        Integer durationSeconds,
        String mapsProvider,
        List<RouteStopResponse> stops
) {}
