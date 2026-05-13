package com.smartcart.shoppingjourney.interfaces.rest;

import com.smartcart.shoppingjourney.application.internal.commandservices.ShoppingCartCommandServiceImpl;
import com.smartcart.shoppingjourney.domain.model.entities.ShoppingCart;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@Tag(name = "Shopping Carts", description = "Shopping Journey Endpoints")
public class ShoppingCartsController {

    private final ShoppingCartCommandServiceImpl shoppingCartCommandService;

    public ShoppingCartsController(ShoppingCartCommandServiceImpl shoppingCartCommandService) {
        this.shoppingCartCommandService = shoppingCartCommandService;
    }

    @PostMapping
    public ResponseEntity<Long> createCart(@RequestParam String customerName) {
        Long cartId = shoppingCartCommandService.createShoppingCart(customerName);
        return ResponseEntity.ok(cartId);
    }
}