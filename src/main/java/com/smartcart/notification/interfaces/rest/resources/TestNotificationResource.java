package com.smartcart.notification.interfaces.rest.resources;

import com.smartcart.notification.domain.model.valueobjects.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestNotificationResource(
        @NotNull Long userId,
        @NotNull ChannelType channel,
        String titulo,
        @NotBlank String cuerpo
) {
}
