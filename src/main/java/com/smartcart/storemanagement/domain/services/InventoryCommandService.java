package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.commands.ApplyProductClearanceCommand;
import com.smartcart.storemanagement.domain.model.commands.ProcessBulkInventoryCommand;

public interface InventoryCommandService {
    BulkResult handle(ProcessBulkInventoryCommand command);
    ClearanceResult handle(ApplyProductClearanceCommand command);
}
