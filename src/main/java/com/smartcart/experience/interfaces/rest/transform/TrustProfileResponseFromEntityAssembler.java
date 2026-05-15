package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.interfaces.rest.resources.TrustProfileResponse;

import java.util.List;

public class TrustProfileResponseFromEntityAssembler {

    public static TrustProfileResponse toResourceFromEntity(Object entity) {
        return new TrustProfileResponse(
                "placeholder-store-id",
                4.5,
                100,
                2,
                List.of("TIENDA_CONFIABLE"),
                java.time.LocalDateTime.now()
        );
    }
}