package com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories;

import com.smartcart.storemanagement.infrastructure.persistence.jpa.readmodel.StoreAnalyticsReadModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreAnalyticsReadModelRepository extends JpaRepository<StoreAnalyticsReadModel, Long> {
}

