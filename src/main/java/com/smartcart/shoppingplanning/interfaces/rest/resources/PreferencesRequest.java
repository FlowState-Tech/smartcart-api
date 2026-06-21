package com.smartcart.shoppingplanning.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

public record PreferencesRequest(List<String> familyBasketSkus, List<Long> preferredStoreIds,
                                 BigDecimal budgetAmount, String budgetCurrency,
                                 Double residenceLat, Double residenceLng) {}
