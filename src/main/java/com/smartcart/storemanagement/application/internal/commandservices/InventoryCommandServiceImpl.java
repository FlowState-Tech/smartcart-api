package com.smartcart.storemanagement.application.internal.commandservices;

import com.smartcart.storemanagement.domain.model.aggregates.Inventory;
import com.smartcart.storemanagement.domain.model.commands.ApplyProductClearanceCommand;
import com.smartcart.storemanagement.domain.model.commands.ProcessBulkInventoryCommand;
import com.smartcart.storemanagement.domain.model.commands.AddInventoryItemCommand;
import com.smartcart.storemanagement.domain.model.events.InventoryBulkSyncCompletedEvent;
import com.smartcart.storemanagement.domain.model.events.PriceUpdatedEvent;
import com.smartcart.storemanagement.domain.model.events.ProductClearanceStartedEvent;
import com.smartcart.storemanagement.domain.model.valueobjects.Sku;
import com.smartcart.storemanagement.domain.services.BulkResult;
import com.smartcart.storemanagement.domain.services.ClearanceResult;
import com.smartcart.storemanagement.domain.services.InventoryBulkProcessorService;
import com.smartcart.storemanagement.domain.services.InventoryCommandService;
import com.smartcart.storemanagement.domain.services.InventoryItemResult;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryCommandServiceImpl implements InventoryCommandService {

    private final InventoryRepository inventoryRepository;
    private final InventoryBulkProcessorService bulkProcessorService = new InventoryBulkProcessorService();

    public InventoryCommandServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public BulkResult handle(ProcessBulkInventoryCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Bulk inventory command is required");
        }
        var inventory = inventoryRepository.findByStoreId(command.storeId())
                .orElseGet(() -> new Inventory(command.storeId()));
        Map<String, com.smartcart.storemanagement.domain.model.valueobjects.Money> oldAmounts = new HashMap<>();
        inventory.getPriceItems().forEach(item -> oldAmounts.put(item.getSku().getCode(), item.getAmount()));

        var result = bulkProcessorService.process(inventory, command.records());

        var storeId = inventory.getStoreId();
        for (var record : command.records()) {
            var sku = new Sku(record.getSku());
            var priceItem = inventory.findPriceItem(sku).orElse(null);
            if (priceItem == null) {
                continue;
            }
            var oldAmount = oldAmounts.get(record.getSku());
            var newAmount = priceItem.getAmount();
            if (oldAmount == null || oldAmount.getAmount().compareTo(newAmount.getAmount()) != 0) {
                inventory.addDomainEvent(new PriceUpdatedEvent(
                        storeId,
                        record.getSku(),
                        newAmount.getAmount(),
                        oldAmount == null ? null : oldAmount.getAmount(),
                        priceItem.isPromotional(),
                        LocalDateTime.now()
                ));
            }
        }

        if (inventory.getId() == null) {
            inventory = inventoryRepository.save(inventory);
        }
        inventory.addDomainEvent(new InventoryBulkSyncCompletedEvent(
                inventory.getId(),
                inventory.getStoreId(),
                result.getProcessed(),
                LocalDateTime.now()
        ));
        inventoryRepository.save(inventory);
        return result;
    }

    @Override
    @Transactional
    public ClearanceResult handle(ApplyProductClearanceCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Clearance command is required");
        }
        var inventory = inventoryRepository.findByStoreId(command.storeId())
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        var product = inventory.getProducts().stream()
                .filter(item -> command.productId().equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        var priceItem = inventory.findPriceItem(product.getSku())
                .orElseThrow(() -> new IllegalArgumentException("Price item not found"));
        priceItem.applyClearance(command.discountPercentage(), command.expiryDate());
        inventory.addDomainEvent(new ProductClearanceStartedEvent(
                inventory.getStoreId(),
                product.getSku().getCode(),
                command.discountPercentage(),
                command.expiryDate(),
                command.reason(),
                LocalDateTime.now()
        ));
        inventoryRepository.save(inventory);
        return new ClearanceResult(
                inventory.getStoreId(),
                product.getSku().getCode(),
                command.discountPercentage(),
                command.expiryDate()
        );
    }

    @Transactional
    @Override
    public InventoryItemResult handle(AddInventoryItemCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Inventory item command is required");
        }
        var inventory = inventoryRepository.findByStoreId(command.storeId())
                .orElseGet(() -> new Inventory(command.storeId()));

        com.smartcart.storemanagement.domain.model.valueobjects.Money oldAmount = null;
        var sku = new Sku(command.sku());
        var existingPrice = inventory.findPriceItem(sku).orElse(null);
        if (existingPrice != null) {
            oldAmount = existingPrice.getAmount();
        }

        var record = new com.smartcart.storemanagement.domain.services.InventoryBulkRecord(
                command.sku(),
                command.name(),
                command.brand(),
                command.categoryId(),
                command.priceAmount(),
                command.currency(),
                command.quantity(),
                command.minThreshold(),
                command.promotional(),
                command.discountPercentage(),
                command.expiryDate()
        );
        bulkProcessorService.process(inventory, java.util.List.of(record));

        var priceItem = inventory.findPriceItem(sku).orElse(null);
        var newAmount = priceItem != null ? priceItem.getAmount() : null;
        if (newAmount != null && (oldAmount == null || oldAmount.getAmount().compareTo(newAmount.getAmount()) != 0)) {
            inventory.addDomainEvent(new PriceUpdatedEvent(
                    inventory.getStoreId(),
                    command.sku(),
                    newAmount.getAmount(),
                    oldAmount == null ? null : oldAmount.getAmount(),
                    priceItem.isPromotional(),
                    LocalDateTime.now()
            ));
        }

        if (inventory.getId() == null) {
            inventory = inventoryRepository.save(inventory);
        }
        inventoryRepository.save(inventory);

        var product = inventory.findProduct(sku).orElse(null);
        var stockPoint = inventory.findStockPoint(sku).orElse(null);
        return new InventoryItemResult(product, priceItem, stockPoint);
    }
}
