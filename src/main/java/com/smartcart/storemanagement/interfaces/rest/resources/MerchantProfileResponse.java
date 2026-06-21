package com.smartcart.storemanagement.interfaces.rest.resources;

import java.util.List;

public record MerchantProfileResponse(Long id,
                                      String username,
                                      boolean isVerified,
                                      Long applicationId,
                                      String companyName,
                                      String ruc,
                                      List<MerchantStoreResponse> stores) {
}
