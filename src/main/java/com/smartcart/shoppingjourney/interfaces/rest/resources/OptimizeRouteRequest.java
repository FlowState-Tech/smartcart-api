package com.smartcart.shoppingjourney.interfaces.rest.resources;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OptimizeRouteRequest(@NotEmpty List<Long> storeIds) {}
