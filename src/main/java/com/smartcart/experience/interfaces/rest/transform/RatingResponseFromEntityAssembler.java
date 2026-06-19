package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.model.entities.Rating;
import com.smartcart.experience.interfaces.rest.resources.RatingResponse;

public class RatingResponseFromEntityAssembler {

    public static RatingResponse toResourceFromEntity(Rating entity) {
        return new RatingResponse(
                entity.getId(),
                entity.getStoreId(),
                entity.getBuyerId(),
                entity.getPuntuacion(),
                entity.getFechaRegistro()
        );
    }
}
