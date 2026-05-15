package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.interfaces.rest.resources.PriceErrorResponse;

public class PriceErrorResponseFromEntityAssembler {

    public static PriceErrorResponse toResourceFromEntity(Object entity) {
        return new PriceErrorResponse(
                "placeholder-id",
                "placeholder-store-id",
                "placeholder-product-id",
                0.0,
                "REPORTADO",
                java.time.LocalDateTime.now()
        );
    }
}