package com.smartcart.experience.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CalculateSavingsResource(
        @NotBlank String buyerId,
        @NotNull @Positive Double precioReferencia,
        @NotNull @Positive Double precioPagado,
        @NotBlank String moneda
) {}