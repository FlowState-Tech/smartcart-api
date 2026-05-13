package com.smartcart.notification.interfaces.rest.transform;

import com.smartcart.notification.domain.model.aggregates.Notification;
import com.smartcart.notification.interfaces.rest.resources.NotificationStatusResponse;

import java.time.Instant;

public class NotificationStatusResponseFromEntityAssembler {
    public static NotificationStatusResponse toResourceFromEntity(Notification n) {
        return new NotificationStatusResponse(
                n.getId(),
                n.getDeliveryState(),
                n.getChannelType(),
                n.getUpdatedAt() != null ? n.getUpdatedAt().toInstant() : Instant.now(),
                n.getAttemptCount()
        );
    }
}
