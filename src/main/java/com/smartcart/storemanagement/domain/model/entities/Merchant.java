package com.smartcart.storemanagement.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "merchants")
@Getter
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, length = 12)
    private String dni;

    @Email
    @Column(nullable = false, length = 120)
    private String email;

    @Column
    private LocalDateTime lastLogin;

    protected Merchant() {
        // For JPA
    }

    public Merchant(String fullName, String dni, String email) {
        updateProfile(fullName, dni, email);
    }

    public void updateProfile(String fullName, String dni, String email) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (!isValidDni(dni)) {
            throw new IllegalArgumentException("DNI must have exactly 8 numeric digits");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        this.fullName = fullName.trim();
        this.dni = dni.trim();
        this.email = email.trim();
    }

    public void verifyIdentity() {
        if (!isValidDni(dni)) {
            throw new IllegalStateException("Merchant identity is not valid");
        }
    }

    public void trackActivity() {
        this.lastLogin = LocalDateTime.now();
    }

    private boolean isValidDni(String dni) {
        return dni != null && dni.trim().matches("\\d{8}");
    }
}

