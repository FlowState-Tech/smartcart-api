package com.smartcart.notification.application.internal.outboundservices.dispatch;

import com.smartcart.notification.domain.model.aggregates.Notification;

/**
 * Outbound port: physical delivery via FCM / SES / SMS provider (stub in dev).
 */
public interface NotificationDispatchGateway {
    /**
     * @return provider reference to persist on success
     */
    String dispatch(Notification notification);
}
