package com.smartcart.storemanagement.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InventoryBulkRecord {
    private final String sku;
    private final String name;
    private final String brand;
    private final Long categoryId;
    private final BigDecimal priceAmount;
    private final String currency;
    private final int quantity;
    private final int minThreshold;
    private final boolean promotional;
    private final BigDecimal discountPercentage;
    private final LocalDate expiryDate;

    public InventoryBulkRecord(String sku, String name, String brand, Long categoryId, BigDecimal priceAmount, String currency,
                               int quantity, int minThreshold, boolean promotional, BigDecimal discountPercentage, LocalDate expiryDate) {
        this.sku = sku;
        this.name = name;
        this.brand = brand;
        this.categoryId = categoryId;
        this.priceAmount = priceAmount;
        this.currency = currency;
        this.quantity = quantity;
        this.minThreshold = minThreshold;
        this.promotional = promotional;
        this.discountPercentage = discountPercentage;
        this.expiryDate = expiryDate;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMinThreshold() {
        return minThreshold;
    }

    public boolean isPromotional() {
        return promotional;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}

