package com.smartcart.shoppingplanning.interfaces.rest.resources;

import java.math.BigDecimal;

public record BarcodeLookupResponse(String sku, String productName, Long storeId, String storeName, BigDecimal price) {}
