package com.smartcart.storemanagement.domain.model.aggregates;

import com.smartcart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.smartcart.storemanagement.domain.model.entities.PriceItem;
import com.smartcart.storemanagement.domain.model.entities.Product;
import com.smartcart.storemanagement.domain.model.entities.StockPoint;
import com.smartcart.storemanagement.domain.model.valueobjects.Sku;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "inventories")
@Getter
public class Inventory extends AuditableAbstractAggregateRoot<Inventory> {

    @Column(nullable = false)
    private Long storeId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceItem> priceItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockPoint> stockPoints = new ArrayList<>();

    protected Inventory() {
        // For JPA
    }

    public Inventory(Long storeId) {
        if (storeId == null || storeId <= 0) {
            throw new IllegalArgumentException("Store id is required");
        }
        this.storeId = storeId;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }
        if (findProduct(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("SKU already exists in inventory");
        }
        this.products.add(product);
    }

    public void addPriceItem(PriceItem priceItem) {
        if (priceItem == null) {
            throw new IllegalArgumentException("Price item is required");
        }
        this.priceItems.removeIf(item -> item.getSku().equals(priceItem.getSku()));
        this.priceItems.add(priceItem);
    }

    public void addStockPoint(StockPoint stockPoint) {
        if (stockPoint == null) {
            throw new IllegalArgumentException("Stock point is required");
        }
        this.stockPoints.removeIf(item -> item.getSku().equals(stockPoint.getSku()));
        this.stockPoints.add(stockPoint);
    }

    public Optional<Product> findProduct(Sku sku) {
        if (sku == null) {
            return Optional.empty();
        }
        return products.stream().filter(product -> sku.equals(product.getSku())).findFirst();
    }

    public Optional<PriceItem> findPriceItem(Sku sku) {
        if (sku == null) {
            return Optional.empty();
        }
        return priceItems.stream().filter(price -> sku.equals(price.getSku())).findFirst();
    }

    public Optional<StockPoint> findStockPoint(Sku sku) {
        if (sku == null) {
            return Optional.empty();
        }
        return stockPoints.stream().filter(stock -> sku.equals(stock.getSku())).findFirst();
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<PriceItem> getPriceItems() {
        return Collections.unmodifiableList(priceItems);
    }

    public List<StockPoint> getStockPoints() {
        return Collections.unmodifiableList(stockPoints);
    }
}

