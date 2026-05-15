package com.smartcart.experience.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.List;

public record TrustProfileResponse(
        String storeId,
        Double trustScore,
        Integer totalCalificaciones,
        Integer erroresDePrecioConfirmados,
        List<String> insignias,
        LocalDateTime ultimaActualizacion
) {}