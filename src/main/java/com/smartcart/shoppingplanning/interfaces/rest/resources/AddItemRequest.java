package com.smartcart.shoppingplanning.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddItemRequest(
        @NotBlank String sku,
        @NotBlank String productName,
        @NotNull @Positive BigDecimal quantity,
        @NotBlank String unit
) {}
