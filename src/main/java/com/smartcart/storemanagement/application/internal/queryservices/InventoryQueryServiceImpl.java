package com.smartcart.storemanagement.application.internal.queryservices;

import com.smartcart.storemanagement.domain.model.aggregates.Inventory;
import com.smartcart.storemanagement.domain.model.queries.GetInventoryByStoreQuery;
import com.smartcart.storemanagement.domain.services.InventoryQueryService;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryQueryServiceImpl implements InventoryQueryService {

    private final InventoryRepository inventoryRepository;

    public InventoryQueryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Optional<Inventory> handle(GetInventoryByStoreQuery query) {
        return inventoryRepository.findByStoreId(query.storeId());
    }
}

