package com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    List<ShoppingList> findByBuyerId(Long buyerId);
}
