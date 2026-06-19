package com.smartcart.shoppingplanning.domain.model.valueobjects;

import java.math.BigDecimal;

public record PriceComparisonResult(
        Long storeId,
        String storeName,
        BigDecimal totalCost,
        String currency,
        int itemsFound,
        int itemsMissing,
        BigDecimal savingsVsWorst,
        BigDecimal savingsPercent,
        boolean withinBudget,
        BigDecimal budgetLimit
) {
    public PriceComparisonResult(Long storeId, String storeName, BigDecimal totalCost, String currency,
                                 int itemsFound, int itemsMissing, BigDecimal savingsVsWorst,
                                 BigDecimal savingsPercent) {
        this(storeId, storeName, totalCost, currency, itemsFound, itemsMissing,
                savingsVsWorst, savingsPercent, true, null);
    }
}
