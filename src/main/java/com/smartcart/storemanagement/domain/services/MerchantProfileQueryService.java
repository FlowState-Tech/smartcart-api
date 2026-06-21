package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.queries.GetMerchantProfileQuery;
import com.smartcart.storemanagement.interfaces.rest.resources.MerchantProfileResponse;

import java.util.Optional;

public interface MerchantProfileQueryService {
    Optional<MerchantProfileResponse> handle(GetMerchantProfileQuery query);
}
