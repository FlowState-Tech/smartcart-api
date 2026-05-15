package com.smartcart.experience.domain.model.commands;

public record ConfirmPriceErrorCommand(
        String storeId,
        String priceErrorId,
        String estado
) {}