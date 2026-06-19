package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.entities.Review;
import com.smartcart.experience.interfaces.rest.resources.ReviewResponse;

public class ReviewResponseFromEntityAssembler {

    public static ReviewResponse toResourceFromEntity(Review entity) {
        return new ReviewResponse(
                entity.getId(),
                entity.getStoreId(),
                entity.getBuyerId(),
                entity.getComentario(),
                entity.getEstadoPublicacion(),
                entity.getRespuesta(),
                entity.getFechaCreacion(),
                entity.getFechaRespuesta()
        );
    }
}
