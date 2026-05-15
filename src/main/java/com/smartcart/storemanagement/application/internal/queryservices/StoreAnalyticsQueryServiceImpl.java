package com.smartcart.storemanagement.application.internal.queryservices;

import com.smartcart.storemanagement.domain.model.queries.GetStoreAnalyticsQuery;
import com.smartcart.storemanagement.domain.services.StoreAnalyticsQueryService;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.readmodel.StoreAnalyticsReadModel;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreAnalyticsReadModelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreAnalyticsQueryServiceImpl implements StoreAnalyticsQueryService {

    private final StoreAnalyticsReadModelRepository analyticsRepository;

    public StoreAnalyticsQueryServiceImpl(StoreAnalyticsReadModelRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public Optional<StoreAnalyticsReadModel> handle(GetStoreAnalyticsQuery query) {
        return analyticsRepository.findById(query.storeId());
    }
}

