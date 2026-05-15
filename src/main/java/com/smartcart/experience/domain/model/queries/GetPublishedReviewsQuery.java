package com.smartcart.experience.domain.model.queries;

public record GetPublishedReviewsQuery(
        String storeId,
        int page,
        int size
) {}