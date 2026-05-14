package com.smartcart.notification.domain.model.commands;

import com.smartcart.notification.domain.model.valueobjects.ChannelType;

/**
 * Send a test notification to validate tokens / channel wiring (report: POST /send-test).
 */
public record SendTestNotificationCommand(Long userId, ChannelType channel, String title, String body) {
}
