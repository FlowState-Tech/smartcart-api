package com.smartcart.storemanagement.infrastructure.persistence.jpa.readmodel;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "store_analytics_model")
@Getter
public class StoreAnalyticsReadModel {

    @Id
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "total_views", nullable = false)
    private long totalViews;

    @Column(name = "abandoned_carts", nullable = false)
    private int abandonedCarts;

    @Column(name = "conversion_rate", nullable = false, precision = 6, scale = 4)
    private BigDecimal conversionRate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "store_analytics_top_products", joinColumns = @JoinColumn(name = "store_id"))
    @OrderColumn(name = "position")
    @Column(name = "sku", nullable = false, length = 50)
    private List<String> topProductSkus = new ArrayList<>();

    protected StoreAnalyticsReadModel() {
        // For JPA
    }

    public StoreAnalyticsReadModel(Long storeId,
                                   long totalViews,
                                   int abandonedCarts,
                                   BigDecimal conversionRate,
                                   List<String> topProductSkus) {
        validate(storeId, conversionRate, topProductSkus);
        this.storeId = storeId;
        this.totalViews = totalViews;
        this.abandonedCarts = abandonedCarts;
        this.conversionRate = conversionRate;
        this.topProductSkus = new ArrayList<>(topProductSkus);
    }

    public void updateMetrics(long totalViews,
                              int abandonedCarts,
                              BigDecimal conversionRate,
                              List<String> topProductSkus) {
        validate(this.storeId, conversionRate, topProductSkus);
        this.totalViews = totalViews;
        this.abandonedCarts = abandonedCarts;
        this.conversionRate = conversionRate;
        this.topProductSkus = new ArrayList<>(topProductSkus);
    }

    public List<String> getTopProductSkus() {
        return Collections.unmodifiableList(topProductSkus);
    }

    private void validate(Long storeId, BigDecimal conversionRate, List<String> topProductSkus) {
        if (storeId == null || storeId <= 0) {
            throw new IllegalArgumentException("Store id is required");
        }
        if (conversionRate == null) {
            throw new IllegalArgumentException("Conversion rate is required");
        }
        if (topProductSkus == null) {
            throw new IllegalArgumentException("Top product skus are required");
        }
    }
}
