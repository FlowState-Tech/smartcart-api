package com.smartcart.experience.domain.model.commands;

public record CalculateSavingsCommand(
        String recorridoId,
        String buyerId,
        Double precioReferencia,
        Double precioPagado,
        String moneda
) {}