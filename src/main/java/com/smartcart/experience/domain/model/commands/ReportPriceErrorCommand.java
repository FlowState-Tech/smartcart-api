package com.smartcart.experience.domain.model.commands;

public record ReportPriceErrorCommand(
        String storeId,
        String buyerId,
        String recorridoId,
        String productoId,
        Double precioDigital,
        Double precioFisico,
        String moneda
) {}