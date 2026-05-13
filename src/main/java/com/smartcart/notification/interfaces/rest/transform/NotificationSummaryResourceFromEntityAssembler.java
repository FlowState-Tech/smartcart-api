package com.smartcart.notification.interfaces.rest.transform;

import com.smartcart.notification.domain.model.aggregates.Notification;
import com.smartcart.notification.interfaces.rest.resources.NotificationSummaryResource;

import java.time.Instant;

public class NotificationSummaryResourceFromEntityAssembler {
    public static NotificationSummaryResource toResourceFromEntity(Notification n) {
        String body = n.getRenderedBody();
        String summary = body.length() > 200 ? body.substring(0, 200) + "…" : body;
        return new NotificationSummaryResource(
                n.getId(),
                n.getChannelType(),
                n.getDeliveryState(),
                n.getRenderedSubject(),
                summary,
                n.getCreatedAt() != null ? n.getCreatedAt().toInstant() : Instant.now(),
                n.getAttemptCount()
        );
    }
}
