package com.smartcart.shoppingplanning.domain.events;

import java.math.BigDecimal;

public record MaxBudgetDefinedEvent(Long buyerId, BigDecimal amount, String currency) {}
