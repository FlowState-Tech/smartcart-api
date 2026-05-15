package com.smartcart.experience.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ReviewResponse(
        String reviewId,
        String storeId,
        String buyerId,
        String comentario,
        String estadoPublicacion,
        String respuesta,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaRespuesta
) {}