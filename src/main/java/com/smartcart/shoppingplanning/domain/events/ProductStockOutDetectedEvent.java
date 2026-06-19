package com.smartcart.shoppingplanning.domain.events;

import java.time.Instant;

public record ProductStockOutDetectedEvent(
        String sku,
        Long storeId,
        Instant occurrenceDate
) {}
