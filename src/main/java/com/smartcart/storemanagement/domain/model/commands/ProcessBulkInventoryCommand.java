package com.smartcart.storemanagement.domain.model.commands;

import com.smartcart.storemanagement.domain.services.InventoryBulkRecord;

import java.util.List;

public record ProcessBulkInventoryCommand(Long storeId, List<InventoryBulkRecord> records) {
}

