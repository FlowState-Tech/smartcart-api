package com.smartcart.storemanagement.domain.model.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceUpdatedEvent(Long storeId,
                                String sku,
                                BigDecimal newAmount,
                                BigDecimal oldAmount,
                                boolean promotional,
                                LocalDateTime occurredAt) {
}

