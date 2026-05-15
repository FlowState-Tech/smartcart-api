package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.commands.AddInventoryItemCommand;
import com.smartcart.storemanagement.domain.model.commands.ApplyProductClearanceCommand;
import com.smartcart.storemanagement.domain.model.commands.ProcessBulkInventoryCommand;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryCommandService {
    BulkResult handle(ProcessBulkInventoryCommand command);
    ClearanceResult handle(ApplyProductClearanceCommand command);

    @Transactional
    InventoryItemResult handle(AddInventoryItemCommand command);
}
