package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.entities.Savings;
import com.smartcart.experience.interfaces.rest.resources.SavingsResponse;

public class SavingsResponseFromEntityAssembler {

    public static SavingsResponse toResourceFromEntity(Savings entity) {
        return new SavingsResponse(
                entity.getId(),
                entity.getRecorridoId(),
                entity.getBuyerId(),
                entity.getMontoAhorrado(),
                entity.getMoneda(),
                entity.getPrecioReferencia(),
                entity.getPrecioPagado()
        );
    }
}
