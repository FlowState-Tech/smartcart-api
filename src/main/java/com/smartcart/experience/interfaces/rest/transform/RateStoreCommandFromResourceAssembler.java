package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.commands.RateStoreCommand;
import com.smartcart.experience.interfaces.rest.resources.RateStoreResource;

public class RateStoreCommandFromResourceAssembler {

    public static RateStoreCommand toCommandFromResource(String storeId, RateStoreResource resource) {
        return new RateStoreCommand(
                storeId,
                resource.buyerId(),
                resource.recorridoId(),
                resource.puntuacion()
        );
    }
}