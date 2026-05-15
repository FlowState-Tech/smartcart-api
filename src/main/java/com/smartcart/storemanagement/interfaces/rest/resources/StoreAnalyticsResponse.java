package com.smartcart.storemanagement.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

public record StoreAnalyticsResponse(Long storeId, Metrics metrics) {

    public record Metrics(long totalViews,
                          int abandonedCarts,
                          BigDecimal conversionRate,
                          List<String> topProducts) {
    }
}

