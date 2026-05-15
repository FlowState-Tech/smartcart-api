package com.smartcart.experience.domain.model.commands;

public record PostReviewCommand(
        String storeId,
        String buyerId,
        String recorridoId,
        String comentario
) {}