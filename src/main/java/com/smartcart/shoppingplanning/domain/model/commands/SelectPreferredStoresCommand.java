package com.smartcart.shoppingplanning.domain.model.commands;
import java.util.List;
public record SelectPreferredStoresCommand(Long buyerId, List<Long> storeIds) {}
