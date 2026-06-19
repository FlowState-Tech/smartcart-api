package com.smartcart.shoppingjourney.infrastructure.external.maps;

import com.smartcart.shoppingjourney.domain.model.valueobjects.RoutePath;
import com.smartcart.shoppingjourney.domain.model.valueobjects.RouteStop;

import java.util.List;

public interface MapsRoutingGateway {
    RoutePath requestPath(double originLat, double originLng, double destLat, double destLng);

    default RoutePath requestMultiStopPath(double originLat, double originLng, List<RouteStop> stops) {
        if (stops.isEmpty()) {
            throw new IllegalArgumentException("At least one stop required");
        }
        int totalDistance = 0;
        int totalDuration = 0;
        var polyline = new StringBuilder();
        double curLat = originLat;
        double curLng = originLng;
        for (var stop : stops) {
            var segment = requestPath(curLat, curLng, stop.latitude(), stop.longitude());
            totalDistance += segment.distanceMeters();
            totalDuration += segment.durationSeconds();
            if (!polyline.isEmpty()) polyline.append(";");
            polyline.append(segment.encodedPolyline());
            curLat = stop.latitude();
            curLng = stop.longitude();
        }
        return new RoutePath(polyline.toString(), totalDistance, totalDuration, segmentProvider(stops));
    }

    private String segmentProvider(List<RouteStop> stops) {
        return "multi-stop";
    }
}
