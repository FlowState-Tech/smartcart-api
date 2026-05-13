package com.smartcart.notification.interfaces.rest.transform;

import com.smartcart.notification.domain.model.commands.UpdatePreferencesCommand;
import com.smartcart.notification.interfaces.rest.resources.UpdatePreferencesResource;

public class UpdatePreferencesCommandFromResourceAssembler {

    public static UpdatePreferencesCommand toCommandFromResource(UpdatePreferencesResource resource) {
        var items = resource.channels().stream()
                .map(ch -> new UpdatePreferencesCommand.ChannelPreferenceItem(
                        ch.tipo(), ch.estaHabilitado(), ch.tokenContacto()))
                .toList();
        var sw = resource.ventanaSilencio();
        return new UpdatePreferencesCommand(
                resource.userId(),
                items,
                sw != null ? sw.horaInicio() : null,
                sw != null ? sw.horaFin() : null
        );
    }
}
