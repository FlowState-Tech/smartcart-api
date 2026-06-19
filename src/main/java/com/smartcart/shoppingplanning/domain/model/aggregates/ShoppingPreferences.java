package com.smartcart.shoppingplanning.domain.model.aggregates;

import com.smartcart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.smartcart.shoppingplanning.domain.model.valueobjects.Budget;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "shopping_preferences")
public class ShoppingPreferences extends AuditableAbstractAggregateRoot<ShoppingPreferences> {

    @Column(name = "buyer_id", nullable = false, unique = true)
    private Long buyerId;

    @ElementCollection
    @CollectionTable(name = "shopping_preferred_stores", joinColumns = @JoinColumn(name = "preferences_id"))
    @Column(name = "store_id")
    private List<Long> preferredStoreIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "shopping_family_basket", joinColumns = @JoinColumn(name = "preferences_id"))
    @Column(name = "sku", length = 50)
    private List<String> familyBasketSkus = new ArrayList<>();

    @Embedded
    private Budget budget;

    @Column(name = "residence_lat")
    private Double residenceLat;

    @Column(name = "residence_lng")
    private Double residenceLng;

    protected ShoppingPreferences() {}

    public ShoppingPreferences(Long buyerId) {
        if (buyerId == null) throw new IllegalArgumentException("Buyer id is required");
        this.buyerId = buyerId;
    }

    public void configureFamilyBasket(List<String> skus) {
        this.familyBasketSkus = new ArrayList<>(skus != null ? skus : List.of());
    }

    public void selectPreferredStores(List<Long> storeIds) {
        this.preferredStoreIds = new ArrayList<>(storeIds != null ? storeIds : List.of());
    }

    public void defineBudget(Budget budget) {
        this.budget = budget;
    }

    public void defineResidence(double lat, double lng) {
        this.residenceLat = lat;
        this.residenceLng = lng;
    }

    public Long getBuyerId() { return buyerId; }
    public List<Long> getPreferredStoreIds() { return Collections.unmodifiableList(preferredStoreIds); }
    public List<String> getFamilyBasketSkus() { return Collections.unmodifiableList(familyBasketSkus); }
    public Budget getBudget() { return budget; }
    public Double getResidenceLat() { return residenceLat; }
    public Double getResidenceLng() { return residenceLng; }
}
