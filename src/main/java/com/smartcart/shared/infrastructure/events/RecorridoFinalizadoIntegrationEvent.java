package com.smartcart.shared.infrastructure.events;

public record RecorridoFinalizadoIntegrationEvent(
        String recorridoId,
        String buyerId,
        Long storeId,
        Long listId
) {}
