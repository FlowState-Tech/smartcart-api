package com.smartcart.shoppingplanning.interfaces.rest.resources;

import java.util.List;

public record CompareBasketResponse(
        List<PriceComparisonResponse> comparisons,
        boolean canastaComparadaEmitted,
        String journeyRoutesEndpoint
) {}
