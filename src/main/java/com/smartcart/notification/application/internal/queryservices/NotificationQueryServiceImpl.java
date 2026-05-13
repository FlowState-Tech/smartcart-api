package com.smartcart.notification.application.internal.queryservices;

import com.smartcart.notification.domain.model.aggregates.Notification;
import com.smartcart.notification.domain.model.queries.GetNotificationHistoryQuery;
import com.smartcart.notification.domain.services.NotificationQueryService;
import com.smartcart.notification.infrastructure.persistence.jpa.repositories.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Page<Notification> handle(GetNotificationHistoryQuery query) {
        var pageable = PageRequest.of(Math.max(query.page(), 0), Math.min(Math.max(query.size(), 1), 100));
        return notificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(query.userId(), pageable);
    }
}
