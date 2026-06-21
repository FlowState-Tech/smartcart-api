package com.smartcart.shoppingplanning.domain.model.entities;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "shopping_list_items")
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String sku;

    @Column(name = "product_name", nullable = false, length = 120)
    private String productName;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(length = 20)
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id", nullable = false)
    private ShoppingList shoppingList;

    protected ShoppingListItem() {}

    public ShoppingListItem(String sku, String productName, BigDecimal quantity, String unit) {
        if (sku == null || sku.isBlank()) throw new IllegalArgumentException("SKU is required");
        if (productName == null || productName.isBlank()) throw new IllegalArgumentException("Product name is required");
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.sku = sku.trim();
        this.productName = productName.trim();
        this.quantity = quantity;
        this.unit = unit != null ? unit.trim() : "und";
    }

    public void assignToList(ShoppingList list) {
        this.shoppingList = list;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public BigDecimal getQuantity() { return quantity; }
    public String getUnit() { return unit; }
}
