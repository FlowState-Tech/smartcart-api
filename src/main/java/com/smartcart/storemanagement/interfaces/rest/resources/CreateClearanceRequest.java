package com.smartcart.storemanagement.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateClearanceRequest(String productId,
                                     BigDecimal discountPercentage,
                                     LocalDate expiryDate,
                                     String reason) {
}

