package com.smartcart.shoppingjourney.infrastructure.external.maps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoogleMapsRoutingAdapterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void parseRoute_sumsAllLegs() throws Exception {
        var json = """
                {
                  "routes": [{
                    "overview_polyline": { "points": "abc123" },
                    "legs": [
                      { "distance": { "value": 1000 }, "duration": { "value": 120 } },
                      { "distance": { "value": 500 }, "duration": { "value": 60 } }
                    ]
                  }]
                }
                """;
        var path = GoogleMapsRoutingAdapter.parseRoute(MAPPER.readTree(json), "google");
        assertEquals("abc123", path.encodedPolyline());
        assertEquals(1500, path.distanceMeters());
        assertEquals(180, path.durationSeconds());
        assertEquals("google", path.provider());
    }
}
