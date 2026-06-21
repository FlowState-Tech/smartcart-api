package com.smartcart.shoppingjourney.infrastructure.external.maps;

import com.smartcart.shoppingjourney.domain.model.valueobjects.RoutePath;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "smartcart.maps.provider", havingValue = "fallback", matchIfMissing = true)
public class FallbackMapsRoutingAdapter implements MapsRoutingGateway {

    @Override
    public RoutePath requestPath(double originLat, double originLng, double destLat, double destLng) {
        var distance = haversineMeters(originLat, originLng, destLat, destLng);
        var duration = (int) Math.max(60, distance / 1.2);
        var polyline = originLat + "," + originLng + ";" + destLat + "," + destLng;
        return new RoutePath(polyline, (int) distance, duration, "fallback");
    }

    static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        var r = 6371000.0;
        var dLat = Math.toRadians(lat2 - lat1);
        var dLon = Math.toRadians(lon2 - lon1);
        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return r * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
