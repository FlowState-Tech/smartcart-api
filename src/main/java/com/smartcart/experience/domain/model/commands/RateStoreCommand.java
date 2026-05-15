package com.smartcart.experience.domain.model.commands;

public record RateStoreCommand(
        String storeId,
        String buyerId,
        String recorridoId,
        Integer puntuacion
) {}