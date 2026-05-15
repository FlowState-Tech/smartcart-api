package com.smartcart.shoppingjourney.interfaces.rest;

import com.smartcart.shoppingjourney.application.internal.commandservices.ShoppingCartCommandServiceImpl;
import com.smartcart.shoppingjourney.interfaces.rest.resources.CreateShoppingCartResource;
import com.smartcart.shoppingjourney.interfaces.rest.resources.ShoppingCartResource;
import com.smartcart.shoppingjourney.interfaces.rest.transform.ShoppingCartResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@Tag(name = "Shopping Carts", description = "Shopping Journey Endpoints")
public class ShoppingCartsController {

    private final ShoppingCartCommandServiceImpl shoppingCartCommandService;

    public ShoppingCartsController(ShoppingCartCommandServiceImpl shoppingCartCommandService) {
        this.shoppingCartCommandService = shoppingCartCommandService;
    }

    @PostMapping
    public ResponseEntity<ShoppingCartResource> createCart(@RequestBody CreateShoppingCartResource resource) {
        Long cartId = shoppingCartCommandService.createShoppingCart(resource.customerName());
        var cart = shoppingCartCommandService.getCartById(cartId);
        
        return cart.map(c -> ResponseEntity.ok(ShoppingCartResourceFromEntityAssembler.toResourceFromEntity(c)))
                   .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartResource>> getAllCarts() {
        var carts = shoppingCartCommandService.getAllCarts();
        var resources = carts.stream()
                             .map(ShoppingCartResourceFromEntityAssembler::toResourceFromEntity)
                             .toList();
        return ResponseEntity.ok(resources);
    }
}