package com.smartcart.storemanagement.application.internal.queryservices;

import com.smartcart.storemanagement.domain.model.aggregates.Store;
import com.smartcart.storemanagement.domain.model.queries.GetStoreProfileQuery;
import com.smartcart.storemanagement.domain.services.StoreQueryService;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreQueryServiceImpl implements StoreQueryService {

    private final StoreRepository storeRepository;

    public StoreQueryServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Optional<Store> handle(GetStoreProfileQuery query) {
        return storeRepository.findById(query.storeId());
    }
}

