package com.smartcart.storemanagement.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClearanceResponse(Long storeId,
                                String sku,
                                BigDecimal discountPercentage,
                                LocalDate expiryDate,
                                String status) {
}

