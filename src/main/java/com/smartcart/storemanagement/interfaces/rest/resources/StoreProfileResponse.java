package com.smartcart.storemanagement.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.List;

public record StoreProfileResponse(Long storeId,
                                   String name,
                                   String ruc,
                                   MerchantResource merchant,
                                   List<BranchResource> branches) {

    public record MerchantResource(String fullName, String dni, String email, LocalDateTime lastLogin) {
    }

    public record BranchResource(AddressResource address, List<OpeningHoursResource> openingHours, boolean active) {
    }

    public record AddressResource(String street, String district, double latitude, double longitude) {
    }

    public record OpeningHoursResource(String day, String open, String close) {
    }
}

