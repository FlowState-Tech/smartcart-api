package com.smartcart.notification.domain.services;

import com.smartcart.notification.domain.model.aggregates.Notification;
import com.smartcart.notification.domain.model.commands.SendTestNotificationCommand;

import java.util.Optional;

public interface NotificationCommandService {
    Optional<Notification> handle(SendTestNotificationCommand command);
}
