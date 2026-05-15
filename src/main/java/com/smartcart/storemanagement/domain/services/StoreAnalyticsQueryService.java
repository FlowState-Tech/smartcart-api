package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.queries.GetStoreAnalyticsQuery;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.readmodel.StoreAnalyticsReadModel;

import java.util.Optional;

public interface StoreAnalyticsQueryService {
    Optional<StoreAnalyticsReadModel> handle(GetStoreAnalyticsQuery query);
}

