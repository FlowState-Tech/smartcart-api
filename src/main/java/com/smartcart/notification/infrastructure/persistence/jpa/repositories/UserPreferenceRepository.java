package com.smartcart.notification.infrastructure.persistence.jpa.repositories;

import com.smartcart.notification.domain.model.aggregates.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    Optional<UserPreference> findByUserId(Long userId);
}
