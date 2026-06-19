package com.smartcart.shoppingplanning.domain.events;

public record SubstituteProductSuggestedEvent(
        String originalSku,
        String substituteSku,
        Long storeId,
        String reason
) {}
