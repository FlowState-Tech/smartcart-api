package com.smartcart.notification.domain.model.queries;

public record GetNotificationHistoryQuery(Long userId, int page, int size) {
}
