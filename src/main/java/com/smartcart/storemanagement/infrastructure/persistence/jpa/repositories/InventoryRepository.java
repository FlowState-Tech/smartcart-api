package com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories;

import com.smartcart.storemanagement.domain.model.aggregates.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByStoreId(Long storeId);
}

