package com.smartcart.storemanagement.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AddInventoryItemCommand(Long storeId,
                                      String sku,
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

