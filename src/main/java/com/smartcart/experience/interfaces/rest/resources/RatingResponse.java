package com.smartcart.experience.interfaces.rest.resources;

import java.time.LocalDateTime;

public record RatingResponse(
        String ratingId,
        String storeId,
        String buyerId,
        Integer puntuacion,
        LocalDateTime fechaRegistro
) {}