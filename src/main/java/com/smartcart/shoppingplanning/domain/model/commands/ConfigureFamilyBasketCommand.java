package com.smartcart.shoppingplanning.domain.model.commands;
import java.util.List;
public record ConfigureFamilyBasketCommand(Long buyerId, List<String> skus) {}
