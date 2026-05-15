package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.aggregates.Store;
import com.smartcart.storemanagement.domain.model.commands.RegisterStoreCommand;

import java.util.Optional;

public interface StoreCommandService {
    Optional<Store> handle(RegisterStoreCommand command);
}

