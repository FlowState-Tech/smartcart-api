package com.smartcart.shoppingplanning.domain.model.commands;
import java.math.BigDecimal;
public record AddItemToListCommand(Long listId, String sku, String productName, BigDecimal quantity, String unit) {}
