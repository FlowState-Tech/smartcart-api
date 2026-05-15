package com.smartcart.experience.interfaces.rest.resources;

import java.time.LocalDateTime;

public record PriceErrorResponse(
        String priceErrorId,
        String storeId,
        String productoId,
        Double discrepancia,
        String estadoError,
        LocalDateTime fechaReporte
) {}