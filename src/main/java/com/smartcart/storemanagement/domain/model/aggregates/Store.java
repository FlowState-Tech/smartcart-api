package com.smartcart.storemanagement.domain.model.aggregates;

import com.smartcart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.smartcart.storemanagement.domain.model.entities.Merchant;
import com.smartcart.storemanagement.domain.model.entities.StoreBranch;
import com.smartcart.storemanagement.domain.model.valueobjects.Ruc;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "stores", uniqueConstraints = @UniqueConstraint(columnNames = "ruc"))
@Getter
public class Store extends AuditableAbstractAggregateRoot<Store> {

    @Column(nullable = false, length = 120)
    private String name;

    @Embedded
    private Ruc ruc;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Merchant merchant;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreBranch> branches = new ArrayList<>();

    protected Store() {
        // For JPA
    }

    public Store(String name, Ruc ruc, Merchant merchant, List<StoreBranch> branches) {
        updateName(name);
        setRuc(ruc);
        setMerchant(merchant);
        setBranches(branches);
    }

    public void updateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Store name is required");
        }
        this.name = name.trim();
    }

    public void setRuc(Ruc ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("RUC is required");
        }
        this.ruc = ruc;
    }

    public void setMerchant(Merchant merchant) {
        if (merchant == null) {
            throw new IllegalArgumentException("Merchant is required");
        }
        merchant.verifyIdentity();
        this.merchant = merchant;
    }

    public void setBranches(List<StoreBranch> branches) {
        if (branches == null || branches.isEmpty()) {
            throw new IllegalArgumentException("At least one branch is required");
        }
        this.branches = new ArrayList<>(branches);
    }

    public StoreBranch registerBranch(StoreBranch branch) {
        if (branch == null) {
            throw new IllegalArgumentException("Branch is required");
        }
        this.branches.add(branch);
        return branch;
    }

    public void closeBranch(Long branchId) {
        if (branchId == null) {
            throw new IllegalArgumentException("Branch id is required");
        }
        branches.stream()
                .filter(branch -> branchId.equals(branch.getId()))
                .findFirst()
                .ifPresent(StoreBranch::closeBranch);
    }

    public List<StoreBranch> getBranches() {
        return Collections.unmodifiableList(branches);
    }
}
