package com.smartcart.shoppingjourney.domain.services;

import org.springframework.stereotype.Service;

@Service
public class GeofencingService {

    private static final double EARTH_RADIUS_M = 6371000.0;
    private static final double DEFAULT_RADIUS_M = 500.0;

    public boolean isWithinStoreRadius(double userLat, double userLng, double storeLat, double storeLng) {
        return haversineMeters(userLat, userLng, storeLat, storeLng) <= DEFAULT_RADIUS_M;
    }

    static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        var dLat = Math.toRadians(lat2 - lat1);
        var dLon = Math.toRadians(lon2 - lon1);
        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return EARTH_RADIUS_M * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
