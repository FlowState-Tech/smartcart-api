package com.smartcart.experience.domain.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store_experiences")
public class StoreExperience {

    @Id
    private String id;

    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Column(name = "buyer_id", nullable = false)
    private String buyerId;

    @Column(name = "recorrido_id", nullable = false)
    private String recorridoId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "storeExperience", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "storeExperience", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "storeExperience", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceError> priceErrors = new ArrayList<>();

    @OneToOne(mappedBy = "storeExperience", cascade = CascadeType.ALL)
    private Savings savings;

    public StoreExperience() {}

    public StoreExperience(String id, String storeId, String buyerId, String recorridoId) {
        this.id = id;
        this.storeId = storeId;
        this.buyerId = buyerId;
        this.recorridoId = recorridoId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
    public String getRecorridoId() { return recorridoId; }
    public void setRecorridoId(String recorridoId) { this.recorridoId = recorridoId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Rating> getRatings() { return ratings; }
    public List<Review> getReviews() { return reviews; }
    public List<PriceError> getPriceErrors() { return priceErrors; }
    public Savings getSavings() { return savings; }
    public void setSavings(Savings savings) { this.savings = savings; }
}