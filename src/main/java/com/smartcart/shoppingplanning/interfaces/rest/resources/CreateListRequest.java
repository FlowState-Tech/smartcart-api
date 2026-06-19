package com.smartcart.shoppingplanning.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateListRequest(
        @NotNull Long buyerId,
        @NotBlank String name
) {}
