package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.aggregates.Inventory;
import com.smartcart.storemanagement.domain.model.queries.GetInventoryByStoreQuery;

import java.util.Optional;

public interface InventoryQueryService {
    Optional<Inventory> handle(GetInventoryByStoreQuery query);
}

