package com.smartcart.storemanagement.interfaces.rest.resources;

import java.util.List;

public record RegisterStoreRequest(String merchantId,
                                   String name,
                                   String ruc,
                                   AddressResource address,
                                   List<OperatingHoursResource> operatingHours) {

    public record AddressResource(String street, String district, double latitude, double longitude) {
    }

    public record OperatingHoursResource(String day, String open, String close) {
    }
}

