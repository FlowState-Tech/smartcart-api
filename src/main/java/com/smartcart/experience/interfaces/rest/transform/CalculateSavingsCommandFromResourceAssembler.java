package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.commands.CalculateSavingsCommand;
import com.smartcart.experience.interfaces.rest.resources.CalculateSavingsResource;

public class CalculateSavingsCommandFromResourceAssembler {

    public static CalculateSavingsCommand toCommandFromResource(String recorridoId, CalculateSavingsResource resource) {
        return new CalculateSavingsCommand(
                recorridoId,
                resource.buyerId(),
                resource.precioReferencia(),
                resource.precioPagado(),
                resource.moneda()
        );
    }
}