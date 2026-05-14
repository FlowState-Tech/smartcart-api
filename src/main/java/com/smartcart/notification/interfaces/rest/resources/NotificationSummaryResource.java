package com.smartcart.notification.interfaces.rest.resources;

import com.smartcart.notification.domain.model.valueobjects.ChannelType;
import com.smartcart.notification.domain.model.valueobjects.DeliveryState;

import java.time.Instant;

public record NotificationSummaryResource(
        Long id,
        ChannelType canal,
        DeliveryState estado,
        String asunto,
        String cuerpoResumen,
        Instant creadoEn,
        int intentos
) {
}
