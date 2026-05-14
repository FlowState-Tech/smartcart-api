package com.smartcart.shoppingjourney.infrastructure.persistence.jpa.repositories;

import com.smartcart.shoppingjourney.domain.model.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    // Aquí es donde va el "extends JpaRepository" y el tipo "Long" (en mayúscula)
}
