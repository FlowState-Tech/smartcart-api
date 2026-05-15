package com.smartcart.storemanagement.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class Address {
    @Column(nullable = false, length = 120)
    private String street;

    @Column(nullable = false, length = 80)
    private String district;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    protected Address() {
        // For JPA
    }

    public Address(String street, String district, double latitude, double longitude) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street is required");
        }
        if (district == null || district.trim().isEmpty()) {
            throw new IllegalArgumentException("District is required");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        this.street = street.trim();
        this.district = district.trim();
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
