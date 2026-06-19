package com.smartcart.shoppingplanning.interfaces.rest.resources;
import java.math.BigDecimal;
public record TotalCostResponse(Long listId, BigDecimal bestTotalCost, String currency) {}
