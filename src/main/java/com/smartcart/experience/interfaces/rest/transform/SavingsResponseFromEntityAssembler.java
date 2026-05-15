package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.interfaces.rest.resources.SavingsResponse;

public class SavingsResponseFromEntityAssembler {

    public static SavingsResponse toResourceFromEntity(Object entity) {
        return new SavingsResponse(
                "placeholder-id",
                "placeholder-recorrido-id",
                "placeholder-buyer-id",
                0.0,
                "PEN",
                0.0,
                0.0
        );
    }
}