package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.entities.PriceError;
import com.smartcart.experience.interfaces.rest.resources.PriceErrorResponse;

public class PriceErrorResponseFromEntityAssembler {

    public static PriceErrorResponse toResourceFromEntity(PriceError entity) {
        return new PriceErrorResponse(
                entity.getId(),
                entity.getStoreId(),
                entity.getProductoId(),
                entity.getDiscrepancia(),
                entity.getEstadoError(),
                entity.getFechaReporte()
        );
    }
}
