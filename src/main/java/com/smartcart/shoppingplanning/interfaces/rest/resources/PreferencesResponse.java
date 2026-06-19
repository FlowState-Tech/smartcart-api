package com.smartcart.shoppingplanning.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

public record PreferencesResponse(Long buyerId, List<String> familyBasketSkus, List<Long> preferredStoreIds,
                                  BigDecimal budgetAmount, String budgetCurrency,
                                  Double residenceLat, Double residenceLng) {}
