package com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories;

import com.smartcart.storemanagement.domain.model.aggregates.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByRucValue(String value);
    boolean existsByRucValue(String value);
    List<Store> findByMerchantEmail(String email);
    List<Store> findByMerchantMerchantId(String merchantId);
}

