package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.commands.ReplyReviewCommand;
import com.smartcart.experience.interfaces.rest.resources.ReplyReviewResource;

public class ReplyReviewCommandFromResourceAssembler {

    public static ReplyReviewCommand toCommandFromResource(String storeId, String reviewId, ReplyReviewResource resource) {
        return new ReplyReviewCommand(
                storeId,
                reviewId,
                resource.merchantId(),
                resource.respuesta()
        );
    }
}