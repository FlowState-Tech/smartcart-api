package com.smartcart.shoppingjourney.interfaces.rest.resources;

public record ShoppingCartResource(Long id, String customerName, Double totalAmount, String status) {
}