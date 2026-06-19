package com.smartcart.shoppingjourney.domain.model.aggregates;

import com.smartcart.shoppingjourney.domain.model.entities.RouteStopEmbeddable;
import com.smartcart.shoppingjourney.domain.model.valueobjects.RouteStop;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shopping_routes")
public class ShoppingRoute {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "list_id")
    private Long listId;

    @Column(name = "residence_lat")
    private Double residenceLat;

    @Column(name = "residence_lng")
    private Double residenceLng;

    @Column(name = "destination_store_id")
    private Long destinationStoreId;

    @Column(name = "destination_lat")
    private Double destinationLat;

    @Column(name = "destination_lng")
    private Double destinationLng;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "encoded_polyline", length = 4000)
    private String encodedPolyline;

    @Column(name = "distance_meters")
    private Integer distanceMeters;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "maps_provider", length = 20)
    private String mapsProvider;

    @Column(name = "current_stop_index", nullable = false)
    private int currentStopIndex = 0;

    @OrderColumn(name = "stop_order")
    @ElementCollection
    @CollectionTable(name = "route_stops", joinColumns = @JoinColumn(name = "route_id"))
    private List<RouteStopEmbeddable> stops = new ArrayList<>();

    protected ShoppingRoute() {}

    public ShoppingRoute(Long buyerId, Long listId) {
        this.id = UUID.randomUUID().toString();
        this.buyerId = buyerId;
        this.listId = listId;
        this.status = "CREATED";
    }

    public void defineResidence(double lat, double lng) {
        this.residenceLat = lat;
        this.residenceLng = lng;
    }

    public void selectDestination(Long storeId, double lat, double lng) {
        this.destinationStoreId = storeId;
        this.destinationLat = lat;
        this.destinationLng = lng;
    }

    public void setOptimizedStops(List<RouteStop> optimized) {
        this.stops = optimized.stream()
                .map(s -> new RouteStopEmbeddable(s.storeId(), s.storeName(), s.latitude(), s.longitude(), s.sequence()))
                .toList();
        if (!optimized.isEmpty()) {
            var last = optimized.getLast();
            selectDestination(last.storeId(), last.latitude(), last.longitude());
        }
        this.status = "OPTIMIZED";
    }

    public void applyPath(String polyline, int distanceMeters, int durationSeconds, String provider) {
        this.encodedPolyline = polyline;
        this.distanceMeters = distanceMeters;
        this.durationSeconds = durationSeconds;
        this.mapsProvider = provider;
        this.status = "PATH_REQUESTED";
    }

    public void startNavigation() {
        if (encodedPolyline == null) throw new IllegalStateException("Request path before navigation");
        this.status = "NAVIGATING";
    }

    public void registerArrival() {
        if (!stops.isEmpty() && currentStopIndex < stops.size() - 1) {
            currentStopIndex++;
            return;
        }
        this.status = "ARRIVED";
    }

    public double[] nextArrivalTarget() {
        if (!stops.isEmpty() && currentStopIndex < stops.size()) {
            var stop = getOrderedStops().get(currentStopIndex);
            return new double[]{stop.latitude(), stop.longitude()};
        }
        if (destinationLat != null && destinationLng != null) {
            return new double[]{destinationLat, destinationLng};
        }
        throw new IllegalStateException("No arrival target on route");
    }

    public int getCurrentStopIndex() { return currentStopIndex; }

    public void finish() {
        this.status = "FINISHED";
    }

    public List<RouteStop> getStops() {
        return stops.stream()
                .map(s -> new RouteStop(s.getStoreId(), s.getStoreName(), s.getLatitude(), s.getLongitude(), s.getSequence()))
                .toList();
    }

    public List<RouteStop> getOrderedStops() {
        return getStops().stream().sorted(Comparator.comparingInt(RouteStop::sequence)).toList();
    }

    public String getId() { return id; }
    public Long getBuyerId() { return buyerId; }
    public Long getListId() { return listId; }
    public Double getResidenceLat() { return residenceLat; }
    public Double getResidenceLng() { return residenceLng; }
    public Long getDestinationStoreId() { return destinationStoreId; }
    public Double getDestinationLat() { return destinationLat; }
    public Double getDestinationLng() { return destinationLng; }
    public String getStatus() { return status; }
    public String getEncodedPolyline() { return encodedPolyline; }
    public Integer getDistanceMeters() { return distanceMeters; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public String getMapsProvider() { return mapsProvider; }
}
