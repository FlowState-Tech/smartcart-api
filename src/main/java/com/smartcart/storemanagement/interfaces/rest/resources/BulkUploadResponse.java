package com.smartcart.storemanagement.interfaces.rest.resources;

import java.time.LocalDateTime;

public record BulkUploadResponse(String jobId,
                                 String status,
                                 int totalItemsProcessed,
                                 int errorsCount,
                                 LocalDateTime timestamp) {
}

