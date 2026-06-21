package com.smartcart.shoppingjourney.domain.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeofencingServiceTest {

    private final GeofencingService geofencing = new GeofencingService();

    @Test
    void acceptsLocationWithin500Meters() {
        assertTrue(geofencing.isWithinStoreRadius(-12.0464, -77.0428, -12.0464, -77.0428));
    }

    @Test
    void rejectsLocationFarFromStore() {
        assertFalse(geofencing.isWithinStoreRadius(-12.0464, -77.0428, -12.1000, -77.1000));
    }
}
