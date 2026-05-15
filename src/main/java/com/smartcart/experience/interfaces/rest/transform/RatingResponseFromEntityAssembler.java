package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.interfaces.rest.resources.RatingResponse;

public class RatingResponseFromEntityAssembler {

    public static RatingResponse toResourceFromEntity(Object entity) {
        // Placeholder - needs entity implementation
        return new RatingResponse(
                "placeholder-id",
                "placeholder-store-id",
                "placeholder-buyer-id",
                5,
                java.time.LocalDateTime.now()
        );
    }
}