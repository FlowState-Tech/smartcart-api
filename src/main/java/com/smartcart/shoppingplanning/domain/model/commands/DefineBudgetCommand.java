package com.smartcart.shoppingplanning.domain.model.commands;
import java.math.BigDecimal;
public record DefineBudgetCommand(Long buyerId, BigDecimal amount, String currency) {}
