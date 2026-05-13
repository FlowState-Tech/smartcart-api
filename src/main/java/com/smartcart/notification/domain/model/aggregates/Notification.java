package com.smartcart.notification.domain.model.aggregates;

import com.smartcart.notification.domain.model.valueobjects.ChannelType;
import com.smartcart.notification.domain.model.valueobjects.DeliveryState;
import com.smartcart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Aggregate root: one notification instance (report §2.6.4).
 */
@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification extends AuditableAbstractAggregateRoot<Notification> {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long recipientUserId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", nullable = false, length = 20)
    private ChannelType channelType;

    @Column(name = "rendered_subject", length = 500)
    private String renderedSubject;

    @NotBlank
    @Column(name = "rendered_body", nullable = false, columnDefinition = "TEXT")
    private String renderedBody;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false, length = 20)
    private DeliveryState deliveryState = DeliveryState.PENDIENTE;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "gateway_reference", length = 256)
    private String gatewayReference;

    public Notification() {
    }

    public Notification(Long recipientUserId, ChannelType channelType, String renderedSubject, String renderedBody) {
        this.recipientUserId = recipientUserId;
        this.channelType = channelType;
        this.renderedSubject = renderedSubject;
        this.renderedBody = renderedBody;
        this.deliveryState = DeliveryState.PENDIENTE;
        this.attemptCount = 0;
    }

    public void markDispatchSuccess(String gatewayReference) {
        this.attemptCount++;
        this.deliveryState = DeliveryState.ENVIADO;
        this.gatewayReference = gatewayReference;
    }

    public void markDispatchFailure() {
        this.attemptCount++;
        this.deliveryState = DeliveryState.FALLIDO;
    }
}
