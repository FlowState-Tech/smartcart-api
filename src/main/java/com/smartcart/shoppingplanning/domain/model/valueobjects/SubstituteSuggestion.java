package com.smartcart.shoppingplanning.domain.model.valueobjects;

public record SubstituteSuggestion(
        String originalSku,
        String substituteSku,
        String substituteName,
        Long storeId,
        String reason
) {}
