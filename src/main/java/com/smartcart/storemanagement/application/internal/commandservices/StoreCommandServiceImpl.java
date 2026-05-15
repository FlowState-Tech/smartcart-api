package com.smartcart.storemanagement.application.internal.commandservices;

import com.smartcart.storemanagement.domain.model.aggregates.Inventory;
import com.smartcart.storemanagement.domain.model.aggregates.Store;
import com.smartcart.storemanagement.domain.model.commands.RegisterStoreCommand;
import com.smartcart.storemanagement.domain.model.entities.Merchant;
import com.smartcart.storemanagement.domain.model.entities.StoreBranch;
import com.smartcart.storemanagement.domain.model.valueobjects.Ruc;
import com.smartcart.storemanagement.domain.services.StoreCommandService;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.InventoryRepository;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StoreCommandServiceImpl implements StoreCommandService {

    private final StoreRepository storeRepository;
    private final InventoryRepository inventoryRepository;

    public StoreCommandServiceImpl(StoreRepository storeRepository, InventoryRepository inventoryRepository) {
        this.storeRepository = storeRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public Optional<Store> handle(RegisterStoreCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Register store command is required");
        }
        var ruc = new Ruc(command.ruc());
        if (storeRepository.existsByRucValue(ruc.getNormalized())) {
            throw new IllegalArgumentException("Store with RUC already exists");
        }
        var merchant = buildPlaceholderMerchant(command.merchantId());
        var branch = new StoreBranch(command.address(), command.openingHours());
        var store = new Store(command.name(), ruc, merchant, List.of(branch));
        var savedStore = storeRepository.save(store);
        inventoryRepository.save(new Inventory(savedStore.getId()));
        return Optional.of(savedStore);
    }

    private Merchant buildPlaceholderMerchant(String merchantId) {
        var normalized = merchantId == null ? "UNKNOWN" : merchantId.trim();
        var email = normalized + "@smartcart.local";
        return new Merchant("Merchant " + normalized, "00000000", email);
    }
}

