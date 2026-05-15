package com.smartcart.storemanagement.interfaces.rest.transform;

import com.smartcart.storemanagement.domain.model.commands.AddInventoryItemCommand;
import com.smartcart.storemanagement.interfaces.rest.resources.CreateInventoryItemRequest;

public class AddInventoryItemCommandFromResourceAssembler {

    public static AddInventoryItemCommand toCommand(Long storeId, CreateInventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Inventory item request is required");
        }
        if (storeId == null || storeId <= 0) {
            throw new IllegalArgumentException("Store id is required");
        }
        return new AddInventoryItemCommand(
                storeId,
                request.sku(),
                request.name(),
                request.brand(),
                request.categoryId(),
                request.priceAmount(),
                request.currency(),
                request.quantity(),
                request.minThreshold(),
                request.promotional(),
                request.discountPercentage(),
                request.expiryDate()
        );
    }
}

