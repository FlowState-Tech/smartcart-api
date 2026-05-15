package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.interfaces.rest.resources.ReviewResponse;

public class ReviewResponseFromEntityAssembler {

    public static ReviewResponse toResourceFromEntity(Object entity) {
        return new ReviewResponse(
                "placeholder-id",
                "placeholder-store-id",
                "placeholder-buyer-id",
                "placeholder-comment",
                "PUBLICADA",
                null,
                java.time.LocalDateTime.now(),
                null
        );
    }
}