package com.smartcart.storemanagement.domain.model.entities;

import com.smartcart.storemanagement.domain.model.valueobjects.Money;
import com.smartcart.storemanagement.domain.model.valueobjects.Sku;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "price_items")
@Getter
public class PriceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Sku sku;

    @Embedded
    private Money amount;

    @Column(nullable = false)
    private boolean promotional;

    @Column
    private LocalDate expiryDate;

    protected PriceItem() {
        // For JPA
    }

    public PriceItem(Sku sku, Money amount) {
        this.sku = sku;
        setAmount(amount);
        this.promotional = false;
    }

    public void setAmount(Money amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        this.amount = amount;
    }

    public void applyClearance(BigDecimal discountPercentage, LocalDate expiryDate) {
        if (expiryDate == null || expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date must be today or in the future");
        }
        this.amount = this.amount.applyPercentageDiscount(discountPercentage);
        this.promotional = true;
        this.expiryDate = expiryDate;
    }

    public boolean validateVigency() {
        return expiryDate == null || !expiryDate.isBefore(LocalDate.now());
    }

    public void markAsExpired() {
        this.promotional = false;
    }
}

