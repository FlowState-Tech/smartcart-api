package com.smartcart.shared.infrastructure.events;

import java.math.BigDecimal;
import java.util.List;

public record BasketComparedIntegrationEvent(
        Long buyerId,
        Long listId,
        Long bestStoreId,
        BigDecimal bestTotalCost,
        List<Long> rankedStoreIds
) {
    public BasketComparedIntegrationEvent(Long buyerId, Long listId, Long bestStoreId, BigDecimal bestTotalCost) {
        this(buyerId, listId, bestStoreId, bestTotalCost, List.of(bestStoreId));
    }
}
