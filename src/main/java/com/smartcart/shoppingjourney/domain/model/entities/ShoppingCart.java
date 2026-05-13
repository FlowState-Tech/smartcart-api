package com.smartcart.shoppingjourney.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private Double totalAmount;

    private String status; // Ej: PENDING, COMPLETED

    public ShoppingCart() {
        this.totalAmount = 0.0;
        this.status = "PENDING";
    }
}