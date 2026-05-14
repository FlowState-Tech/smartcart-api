package com.smartcart.notification.interfaces.rest.resources;

import java.util.List;

public record NotificationHistoryPageResource(
        List<NotificationSummaryResource> content,
        long totalElements,
        int totalPages,
        int number,
        int size
) {
}
