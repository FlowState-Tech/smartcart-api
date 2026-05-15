package com.smartcart.experience.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RateStoreResource(
        @NotBlank String buyerId,
        @NotBlank String recorridoId,
        @NotNull @Min(1) @Max(5) Integer puntuacion
) {}