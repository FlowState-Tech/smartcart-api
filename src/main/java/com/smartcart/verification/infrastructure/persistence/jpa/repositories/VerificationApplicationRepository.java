package com.smartcart.verification.infrastructure.persistence.jpa.repositories;

import com.smartcart.verification.domain.model.aggregates.VerificationApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationApplicationRepository extends JpaRepository<VerificationApplication, Long> {
    Optional<VerificationApplication> findByRucValue(String ruc);
}