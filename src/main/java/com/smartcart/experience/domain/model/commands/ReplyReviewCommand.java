package com.smartcart.experience.domain.model.commands;

public record ReplyReviewCommand(
        String storeId,
        String reviewId,
        String merchantId,
        String respuesta
) {}