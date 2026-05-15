package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.commands.ConfirmPriceErrorCommand;
import com.smartcart.experience.interfaces.rest.resources.ConfirmPriceErrorResource;

public class ConfirmPriceErrorCommandFromResourceAssembler {

    public static ConfirmPriceErrorCommand toCommandFromResource(String storeId, String errorId, ConfirmPriceErrorResource resource) {
        return new ConfirmPriceErrorCommand(
                storeId,
                errorId,
                resource.estado()
        );
    }
}