// Archivo: ShoppingCartCommandServiceImpl.java
package com.smartcart.shoppingjourney.application.internal.commandservices;

import com.smartcart.shoppingjourney.domain.model.entities.ShoppingCart;
import com.smartcart.shoppingjourney.infrastructure.persistence.jpa.repositories.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartCommandServiceImpl { // <--- ELIMINA EL "extends JpaRepository" DE AQUÍ

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartCommandServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public Long createShoppingCart(String customerName) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCustomerName(customerName);
        // Ahora .save() funcionará porque el Repositorio ya es un JpaRepository
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCart.getId();
    }

    public Optional<ShoppingCart> getCartById(Long id) {
        // Ahora .findById() también funcionará
        return shoppingCartRepository.findById(id);
    }
}