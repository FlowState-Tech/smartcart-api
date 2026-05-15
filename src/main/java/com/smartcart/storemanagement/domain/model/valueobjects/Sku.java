package com.smartcart.storemanagement.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class Sku {
    @Column(name = "sku", nullable = false, length = 50)
    private String code;

    protected Sku() {
        // For JPA
    }

    public Sku(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("SKU code is required");
        }
        this.code = code.trim();
    }
}

