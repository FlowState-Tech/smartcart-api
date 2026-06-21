package com.smartcart.shoppingjourney.domain.services;

import com.smartcart.shoppingjourney.domain.model.valueobjects.RouteStop;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteOptimizationServiceTest {

    private final RouteOptimizationService service = new RouteOptimizationService();

    @Test
    void ordersStopsByNearestNeighbor() {
        var stops = List.of(
                new RouteStop(1L, "Far", -12.05, -77.05, 0),
                new RouteStop(2L, "Near", -12.0465, -77.0430, 0));
        var ordered = service.optimize(-12.0464, -77.0428, stops);
        assertEquals(2L, ordered.getFirst().storeId());
        assertEquals(1, ordered.getFirst().sequence());
        assertEquals(1L, ordered.get(1).storeId());
    }
}
