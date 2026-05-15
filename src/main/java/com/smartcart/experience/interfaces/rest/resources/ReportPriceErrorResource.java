package com.smartcart.experience.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReportPriceErrorResource(
        @NotBlank String buyerId,
        @NotBlank String recorridoId,
        @NotBlank String productoId,
        @NotNull @Positive Double precioDigital,
        @NotNull @Positive Double precioFisico,
        @NotBlank String moneda
) {}