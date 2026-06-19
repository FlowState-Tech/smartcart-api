package com.smartcart.shoppingjourney.infrastructure.persistence.jpa.repositories;

import com.smartcart.shoppingjourney.domain.model.aggregates.ShoppingRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ShoppingRouteRepository extends JpaRepository<ShoppingRoute, String> {
    List<ShoppingRoute> findByBuyerIdOrderByIdDesc(Long buyerId);
    List<ShoppingRoute> findByBuyerIdAndListIdOrderByIdDesc(Long buyerId, Long listId);
    Optional<ShoppingRoute> findFirstByBuyerIdAndListIdAndStatusInOrderByIdDesc(
            Long buyerId, Long listId, Collection<String> statuses);
}

