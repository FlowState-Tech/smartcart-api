package com.smartcart.notification.domain.services;

import com.smartcart.notification.domain.model.aggregates.UserPreference;
import com.smartcart.notification.domain.model.commands.UpdatePreferencesCommand;

import java.util.Optional;

public interface UserPreferenceCommandService {
    Optional<UserPreference> handle(UpdatePreferencesCommand command);
}
