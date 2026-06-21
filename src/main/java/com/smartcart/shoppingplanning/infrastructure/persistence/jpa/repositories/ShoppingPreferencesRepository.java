package com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShoppingPreferencesRepository extends JpaRepository<ShoppingPreferences, Long> {
    Optional<ShoppingPreferences> findByBuyerId(Long buyerId);
}
