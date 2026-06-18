package com.smartcart.verification.domain.model.aggregates;

import com.smartcart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.smartcart.verification.domain.model.valueobjects.Ruc;
import com.smartcart.verification.domain.model.valueobjects.VerificationStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "verification_applications")
@Getter
public class VerificationApplication extends AuditableAbstractAggregateRoot<VerificationApplication> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String merchantId;

    @Embedded
    private Ruc ruc;

    @Column(length = 200)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VerificationStatus status;

    protected VerificationApplication() {
        // For JPA
    }

    public VerificationApplication(String merchantId, Ruc ruc) {
        if (merchantId == null || merchantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant id is required");
        }
        if (ruc == null) {
            throw new IllegalArgumentException("RUC is required");
        }
        this.merchantId = merchantId.trim();
        this.ruc = ruc;
        this.status = VerificationStatus.PENDING;
    }

    public void approve(String companyName) {
        this.companyName = companyName != null ? companyName.trim() : "UNKNOWN";
        this.status = VerificationStatus.VERIFIED;
    }

    public void reject() {
        this.status = VerificationStatus.REJECTED;
    }
}