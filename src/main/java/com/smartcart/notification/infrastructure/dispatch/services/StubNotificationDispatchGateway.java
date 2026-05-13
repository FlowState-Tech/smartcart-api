package com.smartcart.notification.infrastructure.dispatch.services;

import com.smartcart.notification.application.internal.outboundservices.dispatch.NotificationDispatchGateway;
import com.smartcart.notification.domain.model.aggregates.Notification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StubNotificationDispatchGateway implements NotificationDispatchGateway {
    @Override
    public String dispatch(Notification notification) {
        return "stub-" + UUID.randomUUID();
    }
}
