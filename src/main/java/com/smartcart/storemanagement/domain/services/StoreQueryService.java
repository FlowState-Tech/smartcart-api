package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.aggregates.Store;
import com.smartcart.storemanagement.domain.model.queries.GetStoreProfileQuery;

import java.util.Optional;

public interface StoreQueryService {
    Optional<Store> handle(GetStoreProfileQuery query);
}

