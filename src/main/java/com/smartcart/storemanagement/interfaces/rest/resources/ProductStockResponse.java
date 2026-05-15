package com.smartcart.storemanagement.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductStockResponse(String sku,
                                   String name,
                                   String brand,
                                   Long categoryId,
                                   boolean active,
                                   BigDecimal priceAmount,
                                   String currency,
                                   boolean promotional,
                                   LocalDate expiryDate,
                                   int quantity,
                                   int minThreshold) {
}

