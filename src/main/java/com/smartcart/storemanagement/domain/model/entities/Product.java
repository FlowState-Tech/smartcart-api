package com.smartcart.storemanagement.domain.model.entities;

import com.smartcart.storemanagement.domain.model.valueobjects.Sku;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "products")
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Sku sku;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 80)
    private String brand;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private boolean active;

    protected Product() {
        // For JPA
    }

    public Product(Sku sku, String name, String brand, Long categoryId) {
        updateDetails(name, brand);
        assignCategory(categoryId);
        this.sku = sku;
        this.active = true;
    }

    public void updateDetails(String name, String brand) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Product brand is required");
        }
        this.name = name.trim();
        this.brand = brand.trim();
    }

    public void assignCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("Category id is required");
        }
        this.categoryId = categoryId;
    }

    public void deactivate() {
        this.active = false;
    }
}

