package com.smartcart.shoppingjourney.infrastructure.external.maps;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartcart.shoppingjourney.domain.model.valueobjects.RoutePath;
import com.smartcart.shoppingjourney.domain.model.valueobjects.RouteStop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "smartcart.maps.provider", havingValue = "google")
public class GoogleMapsRoutingAdapter implements MapsRoutingGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleMapsRoutingAdapter.class);

    private final RestClient restClient;
    private final String apiKey;
    private final FallbackMapsRoutingAdapter fallback = new FallbackMapsRoutingAdapter();

    @org.springframework.beans.factory.annotation.Autowired
    public GoogleMapsRoutingAdapter(@Value("${smartcart.maps.google.api-key:}") String apiKey) {
        this(apiKey, RestClient.create("https://maps.googleapis.com"));
    }

    GoogleMapsRoutingAdapter(String apiKey, RestClient restClient) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.restClient = restClient;
    }

    @PostConstruct
    void logConfiguration() {
        if (apiKey.isBlank()) {
            LOGGER.warn("GOOGLE_MAPS_API_KEY is empty — Directions API calls will use Haversine fallback");
        } else {
            LOGGER.info("Google Maps Directions API configured (provider=google)");
        }
    }

    @Override
    public RoutePath requestPath(double originLat, double originLng, double destLat, double destLng) {
        if (apiKey.isBlank()) {
            return fallback.requestPath(originLat, originLng, destLat, destLng);
        }
        var response = directionsRequest(originLat, originLng, destLat, destLng, null);
        if (response == null) {
            return fallback.requestPath(originLat, originLng, destLat, destLng);
        }
        return parseRoute(response, "google");
    }

    @Override
    public RoutePath requestMultiStopPath(double originLat, double originLng, List<RouteStop> stops) {
        if (stops.isEmpty()) {
            throw new IllegalArgumentException("At least one stop required");
        }
        if (apiKey.isBlank()) {
            return fallback.requestMultiStopPath(originLat, originLng, stops);
        }
        if (stops.size() == 1) {
            var stop = stops.getFirst();
            return requestPath(originLat, originLng, stop.latitude(), stop.longitude());
        }
        var last = stops.getLast();
        var waypoints = stops.subList(0, stops.size() - 1).stream()
                .map(s -> s.latitude() + "," + s.longitude())
                .collect(Collectors.joining("|"));
        var response = directionsRequest(originLat, originLng, last.latitude(), last.longitude(), waypoints);
        if (response == null) {
            return fallback.requestMultiStopPath(originLat, originLng, stops);
        }
        return parseRoute(response, "google");
    }

    private JsonNode directionsRequest(double originLat, double originLng,
                                       double destLat, double destLng, String waypoints) {
        try {
            var response = restClient.get()
                    .uri(uri -> {
                        var builder = uri.path("/maps/api/directions/json")
                                .queryParam("origin", originLat + "," + originLng)
                                .queryParam("destination", destLat + "," + destLng)
                                .queryParam("key", apiKey);
                        if (waypoints != null && !waypoints.isBlank()) {
                            builder.queryParam("waypoints", waypoints);
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .body(JsonNode.class);
            if (response == null || !"OK".equals(response.path("status").asText())) {
                var status = response == null ? "null" : response.path("status").asText("UNKNOWN");
                LOGGER.warn("Google Directions API returned status={}", status);
                return null;
            }
            if (!response.path("routes").isArray() || response.path("routes").isEmpty()) {
                LOGGER.warn("Google Directions API returned no routes");
                return null;
            }
            return response;
        } catch (Exception ex) {
            LOGGER.warn("Google Directions API call failed: {}", ex.getMessage());
            return null;
        }
    }

    static RoutePath parseRoute(JsonNode response, String provider) {
        var route = response.path("routes").get(0);
        var polyline = route.path("overview_polyline").path("points").asText("");
        int totalDistance = 0;
        int totalDuration = 0;
        for (var leg : route.path("legs")) {
            totalDistance += leg.path("distance").path("value").asInt();
            totalDuration += leg.path("duration").path("value").asInt();
        }
        return new RoutePath(polyline, totalDistance, totalDuration, provider);
    }
}
