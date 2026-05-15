package com.smartcart.storemanagement.domain.model.entities;

import com.smartcart.storemanagement.domain.model.valueobjects.Address;
import com.smartcart.storemanagement.domain.model.valueobjects.OpeningHours;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "store_branches")
@Getter
public class StoreBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @ElementCollection
    @CollectionTable(name = "store_branch_opening_hours", joinColumns = @JoinColumn(name = "branch_id"))
    private List<OpeningHours> openingHours = new ArrayList<>();

    @Column(nullable = false)
    private boolean active;

    protected StoreBranch() {
        // For JPA
    }

    public StoreBranch(Address address, List<OpeningHours> openingHours) {
        updateLocation(address);
        setOpeningHours(openingHours);
        this.active = true;
    }

    public void openBranch() {
        this.active = true;
    }

    public void closeBranch() {
        this.active = false;
    }

    public void updateLocation(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }
        this.address = address;
    }

    public void setOpeningHours(List<OpeningHours> openingHours) {
        if (openingHours == null || openingHours.isEmpty()) {
            throw new IllegalArgumentException("Opening hours are required");
        }
        this.openingHours = new ArrayList<>(openingHours);
    }

    public List<OpeningHours> getOpeningHours() {
        return Collections.unmodifiableList(openingHours);
    }
}

