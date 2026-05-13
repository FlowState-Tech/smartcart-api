package com.smartcart.notification.interfaces.rest.resources;

import com.smartcart.notification.domain.model.valueobjects.ChannelType;
import com.smartcart.notification.domain.model.valueobjects.DeliveryState;

import java.time.Instant;

public record NotificationStatusResponse(
        Long notificationId,
        DeliveryState estado,
        ChannelType canal,
        Instant fechaEnvio,
        int intentos
) {
}
