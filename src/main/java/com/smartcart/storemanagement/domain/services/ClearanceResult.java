package com.smartcart.storemanagement.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClearanceResult(Long storeId,
                              String sku,
                              BigDecimal discountPercentage,
                              LocalDate expiryDate) {
}

