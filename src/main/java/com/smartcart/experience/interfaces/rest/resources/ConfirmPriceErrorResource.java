package com.smartcart.experience.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ConfirmPriceErrorResource(
        @NotBlank @Pattern(regexp = "CONFIRMADO|RECHAZADO") String estado
) {}