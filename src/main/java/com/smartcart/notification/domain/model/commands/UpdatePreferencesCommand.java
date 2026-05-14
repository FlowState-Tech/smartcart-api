package com.smartcart.notification.domain.model.commands;

import com.smartcart.notification.domain.model.valueobjects.ChannelType;

import java.time.LocalTime;
import java.util.List;

/**
 * Replace user communication preferences (channels + optional silence window).
 */
public record UpdatePreferencesCommand(
        Long userId,
        List<ChannelPreferenceItem> channels,
        LocalTime silenceWindowStart,
        LocalTime silenceWindowEnd
) {
    public record ChannelPreferenceItem(ChannelType tipo, boolean estaHabilitado, String tokenContacto) {
    }
}
