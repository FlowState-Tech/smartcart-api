package com.smartcart.storemanagement.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateInventoryItemRequest(String sku,
                                         String name,
                                         String brand,
                                         Long categoryId,
                                         BigDecimal priceAmount,
                                         String currency,
                                         int quantity,
                                         int minThreshold,
                                         boolean promotional,
                                         BigDecimal discountPercentage,
                                         LocalDate expiryDate) {
}

