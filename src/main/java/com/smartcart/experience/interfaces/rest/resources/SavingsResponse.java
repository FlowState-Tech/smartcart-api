package com.smartcart.experience.interfaces.rest.resources;

public record SavingsResponse(
        String savingsId,
        String recorridoId,
        String buyerId,
        Double montoAhorrado,
        String moneda,
        Double precioReferencia,
        Double precioPagado
) {}