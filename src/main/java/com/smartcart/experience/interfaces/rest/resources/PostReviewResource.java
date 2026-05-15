package com.smartcart.experience.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostReviewResource(
        @NotBlank String buyerId,
        @NotBlank String recorridoId,
        @NotNull @Size(min = 10, max = 1000) String comentario
) {}