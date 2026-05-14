package com.smartcart.notification.interfaces.rest.transform;

import com.smartcart.notification.domain.model.aggregates.UserPreference;
import com.smartcart.notification.interfaces.rest.resources.PreferenceResponse;

import java.time.Instant;

public class PreferenceResponseFromEntityAssembler {
    public static PreferenceResponse toResourceFromEntity(UserPreference pref) {
        var channels = pref.getChannels().stream()
                .map(c -> new PreferenceResponse.ChannelItem(
                        c.getChannelType(), c.isEnabled(), c.getContactToken()))
                .toList();
        PreferenceResponse.SilenceWindow silence = null;
        if (pref.getSilenceWindowStart() != null && pref.getSilenceWindowEnd() != null) {
            silence = new PreferenceResponse.SilenceWindow(
                    pref.getSilenceWindowStart().toString(),
                    pref.getSilenceWindowEnd().toString());
        }
        return new PreferenceResponse(
                pref.getUserId(),
                channels,
                silence,
                pref.getUpdatedAt() != null ? pref.getUpdatedAt().toInstant() : Instant.now()
        );
    }
}
