package com.smartcart.shoppingplanning.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Budget {

    @Column(name = "budget_amount")
    private BigDecimal amount;

    @Column(name = "budget_currency", length = 3)
    private String currency;

    protected Budget() {}

    public Budget(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget amount must be positive");
        }
        this.amount = amount;
        this.currency = currency != null ? currency : "PEN";
    }

    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
}
