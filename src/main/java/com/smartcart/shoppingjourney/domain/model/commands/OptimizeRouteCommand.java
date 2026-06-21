package com.smartcart.shoppingjourney.domain.model.commands;

import java.util.List;

public record OptimizeRouteCommand(String routeId, List<Long> storeIds) {}
