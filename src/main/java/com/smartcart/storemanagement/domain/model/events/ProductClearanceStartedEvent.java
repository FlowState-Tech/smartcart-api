package com.smartcart.storemanagement.domain.model.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductClearanceStartedEvent(Long storeId,
                                           String sku,
                                           BigDecimal discountPercentage,
                                           LocalDate expiryDate,
                                           String reason,
                                           LocalDateTime occurredAt) {
}

