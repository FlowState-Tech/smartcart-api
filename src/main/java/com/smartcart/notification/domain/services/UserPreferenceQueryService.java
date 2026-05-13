package com.smartcart.notification.domain.services;

import com.smartcart.notification.domain.model.aggregates.UserPreference;
import com.smartcart.notification.domain.model.queries.GetUserPreferencesQuery;

import java.util.Optional;

public interface UserPreferenceQueryService {
    Optional<UserPreference> handle(GetUserPreferencesQuery query);
}
