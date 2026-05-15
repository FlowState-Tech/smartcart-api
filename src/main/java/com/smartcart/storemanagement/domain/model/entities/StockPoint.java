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

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_points")
@Getter
public class StockPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Sku sku;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int minThreshold;

    @Column
    private LocalDateTime lastChecked;

    protected StockPoint() {
        // For JPA
    }

    public StockPoint(Sku sku, int quantity, int minThreshold) {
        if (sku == null) {
            throw new IllegalArgumentException("SKU is required");
        }
        validateThreshold(minThreshold);
        validateQuantity(quantity);
        this.sku = sku;
        this.quantity = quantity;
        this.minThreshold = minThreshold;
        this.lastChecked = LocalDateTime.now();
    }

    public void updateStock(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
        this.lastChecked = LocalDateTime.now();
    }

    public void replenish(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Replenish quantity must be greater than zero");
        }
        this.quantity += quantity;
        this.lastChecked = LocalDateTime.now();
    }

    public boolean checkLowStock() {
        this.lastChecked = LocalDateTime.now();
        return quantity < minThreshold;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity must be greater than or equal to zero");
        }
    }

    private void validateThreshold(int minThreshold) {
        if (minThreshold < 0) {
            throw new IllegalArgumentException("Minimum threshold must be greater than or equal to zero");
        }
    }
}

