package com.smartcart.shoppingjourney.domain.services;

import com.smartcart.shoppingjourney.domain.model.aggregates.ShoppingRoute;
import com.smartcart.shoppingjourney.domain.model.valueobjects.RoutePath;
import com.smartcart.shoppingjourney.domain.model.valueobjects.RouteStop;
import com.smartcart.shoppingjourney.infrastructure.external.maps.MapsRoutingGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Política de Integración con Mapas (Event Storming).
 */
@Component
public class MapsIntegrationPolicy {

    private final MapsRoutingGateway mapsGateway;

    public MapsIntegrationPolicy(MapsRoutingGateway mapsGateway) {
        this.mapsGateway = mapsGateway;
    }

    public RoutePath requestPath(ShoppingRoute route) {
        if (route.getResidenceLat() == null || route.getDestinationLat() == null) {
            throw new IllegalStateException("Residence and destination required for Maps policy");
        }
        if (route.getStops().isEmpty()) {
            return mapsGateway.requestPath(
                    route.getResidenceLat(), route.getResidenceLng(),
                    route.getDestinationLat(), route.getDestinationLng());
        }
        return mapsGateway.requestMultiStopPath(
                route.getResidenceLat(), route.getResidenceLng(), route.getOrderedStops());
    }
}
