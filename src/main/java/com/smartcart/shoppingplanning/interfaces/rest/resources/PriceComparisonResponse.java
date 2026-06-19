package com.smartcart.shoppingplanning.interfaces.rest.resources;

import java.math.BigDecimal;

public record PriceComparisonResponse(
        Long storeId,
        String storeName,
        BigDecimal totalCost,
        String currency,
        int itemsFound,
        int itemsMissing,
        BigDecimal savings,
        BigDecimal savingsPercent,
        boolean withinBudget,
        BigDecimal budgetLimit
) {}
