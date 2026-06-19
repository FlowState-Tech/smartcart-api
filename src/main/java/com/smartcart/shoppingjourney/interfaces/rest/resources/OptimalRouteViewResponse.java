package com.smartcart.shoppingjourney.interfaces.rest.resources;

import java.util.List;

public record OptimalRouteViewResponse(
        String routeId,
        String status,
        String encodedPolyline,
        Integer distanceMeters,
        Integer durationSeconds,
        String mapsProvider,
        List<RouteStopResponse> stops
) {}
