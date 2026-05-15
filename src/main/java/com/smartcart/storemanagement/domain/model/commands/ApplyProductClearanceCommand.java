package com.smartcart.storemanagement.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ApplyProductClearanceCommand(Long storeId,
                                           Long productId,
                                           BigDecimal discountPercentage,
                                           LocalDate expiryDate,
                                           String reason) {
}

