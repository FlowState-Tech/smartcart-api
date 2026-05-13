package com.smartcart.notification.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartcart.notification.domain.model.valueobjects.ChannelType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdatePreferencesResource(
        @NotNull Long userId,
        @NotEmpty @Valid List<ChannelResource> channels,
        SilenceWindowResource ventanaSilencio
) {
    public record ChannelResource(@NotNull ChannelType tipo, boolean estaHabilitado, String tokenContacto) {
    }

    public record SilenceWindowResource(LocalTime horaInicio, LocalTime horaFin) {
    }
}
