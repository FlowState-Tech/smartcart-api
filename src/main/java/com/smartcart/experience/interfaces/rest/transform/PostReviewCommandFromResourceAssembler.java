package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.commands.PostReviewCommand;
import com.smartcart.experience.interfaces.rest.resources.PostReviewResource;

public class PostReviewCommandFromResourceAssembler {

    public static PostReviewCommand toCommandFromResource(String storeId, PostReviewResource resource) {
        return new PostReviewCommand(
                storeId,
                resource.buyerId(),
                resource.recorridoId(),
                resource.comentario()
        );
    }
}