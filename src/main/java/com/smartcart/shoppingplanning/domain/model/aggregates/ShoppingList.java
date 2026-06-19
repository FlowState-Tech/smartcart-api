package com.smartcart.shoppingplanning.domain.model.aggregates;

import com.smartcart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.smartcart.shoppingplanning.domain.model.entities.ShoppingListItem;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "shopping_lists")
public class ShoppingList extends AuditableAbstractAggregateRoot<ShoppingList> {

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(length = 120)
    private String name;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingListItem> items = new ArrayList<>();

    protected ShoppingList() {}

    public ShoppingList(Long buyerId, String name) {
        if (buyerId == null) throw new IllegalArgumentException("Buyer id is required");
        this.buyerId = buyerId;
        this.name = name != null ? name.trim() : "Mi canasta";
    }

    public void addItem(ShoppingListItem item) {
        item.assignToList(this);
        items.add(item);
    }

    public void removeItem(Long itemId) {
        items.removeIf(i -> i.getId().equals(itemId));
    }

    public Long getBuyerId() { return buyerId; }
    public String getName() { return name; }
    public List<ShoppingListItem> getItems() { return Collections.unmodifiableList(items); }
}
