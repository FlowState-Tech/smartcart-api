package com.smartcart.notification.domain.model.valueobjects;

/**
 * Lifecycle state of a notification delivery ({@code EstadoEnvio} in domain language).
 */
public enum DeliveryState {
    PENDIENTE,
    ENVIADO,
    FALLIDO,
    LEIDO
}
