package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.aggregates.Inventory;
import com.smartcart.storemanagement.domain.model.entities.PriceItem;
import com.smartcart.storemanagement.domain.model.entities.Product;
import com.smartcart.storemanagement.domain.model.entities.StockPoint;
import com.smartcart.storemanagement.domain.model.valueobjects.Money;
import com.smartcart.storemanagement.domain.model.valueobjects.Sku;

import java.util.ArrayList;
import java.util.List;

public class InventoryBulkProcessorService {

    public BulkResult process(Inventory inventory, List<InventoryBulkRecord> records) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory is required");
        }
        if (records == null) {
            throw new IllegalArgumentException("Records are required");
        }

        int processed = 0;
        int failed = 0;
        List<String> errors = new ArrayList<>();

        for (var record : records) {
            try {
                applyRecord(inventory, record);
                processed++;
            } catch (Exception ex) {
                failed++;
                errors.add(ex.getMessage());
            }
        }

        return new BulkResult(processed, failed, errors);
    }

    private void applyRecord(Inventory inventory, InventoryBulkRecord record) {
        var sku = new Sku(record.getSku());
        var money = new Money(record.getPriceAmount(), record.getCurrency());

        var product = inventory.findProduct(sku)
                .orElseGet(() -> {
                    var created = new Product(sku, record.getName(), record.getBrand(), record.getCategoryId());
                    inventory.addProduct(created);
                    return created;
                });
        product.updateDetails(record.getName(), record.getBrand());
        product.assignCategory(record.getCategoryId());

        var priceItem = inventory.findPriceItem(sku)
                .orElseGet(() -> {
                    var created = new PriceItem(sku, money);
                    inventory.addPriceItem(created);
                    return created;
                });
        priceItem.setAmount(money);
        if (record.isPromotional()) {
            priceItem.applyClearance(record.getDiscountPercentage(), record.getExpiryDate());
        }

        var stockPoint = inventory.findStockPoint(sku)
                .orElseGet(() -> {
                    var created = new StockPoint(sku, record.getQuantity(), record.getMinThreshold());
                    inventory.addStockPoint(created);
                    return created;
                });
        stockPoint.updateStock(record.getQuantity());
    }
}

