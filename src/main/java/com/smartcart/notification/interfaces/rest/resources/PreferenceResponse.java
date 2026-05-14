package com.smartcart.notification.interfaces.rest.resources;

import com.smartcart.notification.domain.model.valueobjects.ChannelType;

import java.time.Instant;
import java.util.List;

public record PreferenceResponse(
        Long userId,
        List<ChannelItem> channels,
        SilenceWindow silenceWindow,
        Instant updatedAt
) {
    public record ChannelItem(ChannelType tipo, boolean estaHabilitado, String tokenContacto) {
    }

    public record SilenceWindow(String horaInicio, String horaFin) {
    }
}
