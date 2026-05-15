package com.smartcart.shoppingjourney.infrastructure.persistence.jpa.repositories;

import com.smartcart.shoppingjourney.domain.model.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByCustomerName(String customerName);
}