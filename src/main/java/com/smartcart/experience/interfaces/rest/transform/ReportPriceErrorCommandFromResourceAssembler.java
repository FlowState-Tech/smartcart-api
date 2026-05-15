package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.commands.ReportPriceErrorCommand;
import com.smartcart.experience.interfaces.rest.resources.ReportPriceErrorResource;

public class ReportPriceErrorCommandFromResourceAssembler {

    public static ReportPriceErrorCommand toCommandFromResource(String storeId, ReportPriceErrorResource resource) {
        return new ReportPriceErrorCommand(
                storeId,
                resource.buyerId(),
                resource.recorridoId(),
                resource.productoId(),
                resource.precioDigital(),
                resource.precioFisico(),
                resource.moneda()
        );
    }
}