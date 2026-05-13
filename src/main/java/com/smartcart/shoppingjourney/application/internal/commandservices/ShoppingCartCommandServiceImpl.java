package com.smartcart.shoppingjourney.application.internal.commandservices;

import com.smartcart.shoppingjourney.domain.model.entities.ShoppingCart;
import com.smartcart.shoppingjourney.infrastructure.persistence.jpa.repositories.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartCommandServiceImpl {

    private final ShoppingCartRepository shoppingCartRepository;

    // Inyección por constructor (lo más seguro en Spring)
    public ShoppingCartCommandServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public Long createShoppingCart(String customerName) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCustomerName(customerName);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCart.getId();
    }

    public Optional<ShoppingCart> getCartById(Long id) {
        return shoppingCartRepository.findById(id);
    }
}