package com.smartcart.shoppingjourney.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RouteStopEmbeddable {

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "store_name", length = 120)
    private String storeName;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private int sequence;

    protected RouteStopEmbeddable() {}

    public RouteStopEmbeddable(Long storeId, String storeName, double latitude, double longitude, int sequence) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sequence = sequence;
    }

    public Long getStoreId() { return storeId; }
    public String getStoreName() { return storeName; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getSequence() { return sequence; }
}
