package com.smartcart.notification.application.internal.queryservices;

import com.smartcart.notification.domain.model.aggregates.UserPreference;
import com.smartcart.notification.domain.model.queries.GetUserPreferencesQuery;
import com.smartcart.notification.domain.services.UserPreferenceQueryService;
import com.smartcart.notification.infrastructure.persistence.jpa.repositories.UserPreferenceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPreferenceQueryServiceImpl implements UserPreferenceQueryService {

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceQueryServiceImpl(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Override
    public Optional<UserPreference> handle(GetUserPreferencesQuery query) {
        return userPreferenceRepository.findByUserId(query.userId());
    }
}
