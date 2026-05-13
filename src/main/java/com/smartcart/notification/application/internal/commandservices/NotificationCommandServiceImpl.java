package com.smartcart.notification.application.internal.commandservices;

import com.smartcart.notification.application.internal.outboundservices.dispatch.NotificationDispatchGateway;
import com.smartcart.notification.domain.model.aggregates.Notification;
import com.smartcart.notification.domain.model.aggregates.UserPreference;
import com.smartcart.notification.domain.model.commands.SendTestNotificationCommand;
import com.smartcart.notification.domain.services.NotificationCommandService;
import com.smartcart.notification.infrastructure.persistence.jpa.repositories.NotificationRepository;
import com.smartcart.notification.infrastructure.persistence.jpa.repositories.UserPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationDispatchGateway dispatchGateway;

    public NotificationCommandServiceImpl(UserPreferenceRepository userPreferenceRepository,
                                          NotificationRepository notificationRepository,
                                          NotificationDispatchGateway dispatchGateway) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.notificationRepository = notificationRepository;
        this.dispatchGateway = dispatchGateway;
    }

    @Override
    @Transactional
    public Optional<Notification> handle(SendTestNotificationCommand command) {
        UserPreference pref = userPreferenceRepository.findByUserId(command.userId())
                .orElseThrow(() -> new RuntimeException("User preferences not found; configure preferences first"));
        if (!pref.isChannelEnabled(command.channel())) {
            throw new RuntimeException("Channel is disabled for this user");
        }
        var notification = new Notification(
                command.userId(),
                command.channel(),
                command.title(),
                command.body()
        );
        notificationRepository.save(notification);
        try {
            String ref = dispatchGateway.dispatch(notification);
            notification.markDispatchSuccess(ref);
        } catch (RuntimeException ex) {
            notification.markDispatchFailure();
        }
        return Optional.of(notificationRepository.save(notification));
    }
}
