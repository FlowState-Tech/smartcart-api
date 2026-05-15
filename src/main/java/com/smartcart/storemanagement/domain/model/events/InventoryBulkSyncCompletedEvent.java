package com.smartcart.storemanagement.domain.model.events;

import java.time.LocalDateTime;

public record InventoryBulkSyncCompletedEvent(Long inventoryId,
                                              Long storeId,
                                              int totalItemsProcessed,
                                              LocalDateTime occurredAt) {
}

