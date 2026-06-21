package com.smartcart.shoppingjourney.domain.services;

import com.smartcart.shoppingjourney.domain.model.valueobjects.RouteStop;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RouteOptimizationService {

    public List<RouteStop> optimize(double originLat, double originLng, List<RouteStop> stops) {
        if (stops.size() <= 1) return stops;
        var remaining = new ArrayList<>(stops);
        var ordered = new ArrayList<RouteStop>();
        double curLat = originLat;
        double curLng = originLng;
        int seq = 1;
        while (!remaining.isEmpty()) {
            final double lat = curLat;
            final double lng = curLng;
            var nearest = remaining.stream()
                    .min(Comparator.comparingDouble(s -> GeofencingService.haversineMeters(
                            lat, lng, s.latitude(), s.longitude())))
                    .orElseThrow();
            ordered.add(new RouteStop(nearest.storeId(), nearest.storeName(), nearest.latitude(),
                    nearest.longitude(), seq++));
            remaining.remove(nearest);
            curLat = nearest.latitude();
            curLng = nearest.longitude();
        }
        return ordered;
    }
}
