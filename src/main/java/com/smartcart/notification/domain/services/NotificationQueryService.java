package com.smartcart.notification.domain.services;

import com.smartcart.notification.domain.model.aggregates.Notification;
import com.smartcart.notification.domain.model.queries.GetNotificationHistoryQuery;
import org.springframework.data.domain.Page;

public interface NotificationQueryService {
    Page<Notification> handle(GetNotificationHistoryQuery query);
}
