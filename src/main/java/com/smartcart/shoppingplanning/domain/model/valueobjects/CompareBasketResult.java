package com.smartcart.shoppingplanning.domain.model.valueobjects;

import java.util.List;

public record CompareBasketResult(
        List<PriceComparisonResult> comparisons,
        boolean canastaComparadaEmitted,
        String journeyRoutesEndpoint
) {}
