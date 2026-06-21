package com.smartcart.shoppingplanning.domain.model.valueobjects;

import java.math.BigDecimal;

public record BarcodeLookupResult(String sku, String productName, Long storeId, String storeName, BigDecimal price) {}
