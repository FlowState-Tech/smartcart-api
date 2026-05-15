package com.smartcart.storemanagement.domain.model.queries;

public record GetInventoryByStoreQuery(Long storeId, Long categoryId, String sku) {
}

