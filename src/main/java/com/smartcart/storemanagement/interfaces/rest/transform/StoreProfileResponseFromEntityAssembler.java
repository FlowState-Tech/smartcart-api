package com.smartcart.storemanagement.interfaces.rest.transform;

import com.smartcart.storemanagement.domain.model.aggregates.Store;
import com.smartcart.storemanagement.interfaces.rest.resources.StoreProfileResponse;

import java.util.List;

public class StoreProfileResponseFromEntityAssembler {

    public static StoreProfileResponse toResourceFromEntity(Store store) {
        var merchant = store.getMerchant();
        var merchantResource = new StoreProfileResponse.MerchantResource(
                merchant.getFullName(),
                merchant.getDni(),
                merchant.getEmail(),
                merchant.getLastLogin()
        );
        List<StoreProfileResponse.BranchResource> branches = store.getBranches().stream()
                .map(branch -> {
                    var address = branch.getAddress();
                    var addressResource = new StoreProfileResponse.AddressResource(
                            address.getStreet(),
                            address.getDistrict(),
                            address.getLatitude(),
                            address.getLongitude()
                    );
                    var hours = branch.getOpeningHours().stream()
                            .map(openingHours -> new StoreProfileResponse.OpeningHoursResource(
                                    openingHours.getDayOfWeek().name(),
                                    openingHours.getOpenTime().toString(),
                                    openingHours.getCloseTime().toString()
                            ))
                            .toList();
                    return new StoreProfileResponse.BranchResource(addressResource, hours, branch.isActive());
                })
                .toList();
        return new StoreProfileResponse(
                store.getId(),
                store.getName(),
                store.getRuc().getNormalized(),
                merchantResource,
                branches
        );
    }
}

