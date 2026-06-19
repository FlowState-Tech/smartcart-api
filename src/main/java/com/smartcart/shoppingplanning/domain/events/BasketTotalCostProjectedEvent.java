package com.smartcart.shoppingplanning.domain.events;

import java.math.BigDecimal;
import java.util.List;

/**
 * Canasta Comparada — evento de dominio (Event Storming / reporte 2.6.5).
 */
public record BasketTotalCostProjectedEvent(
        Long buyerId,
        Long listId,
        Long bestStoreId,
        BigDecimal bestTotalCost,
        List<Long> rankedStoreIds
) {}
