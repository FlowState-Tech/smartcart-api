package com.smartcart.storemanagement.domain.model.valueobjects;

import com.smartcart.storemanagement.domain.model.exceptions.InvalidRucException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
@Getter
@EqualsAndHashCode
public class Ruc {
    private static final Pattern RUC_PATTERN = Pattern.compile("\\d{11}");

    @Column(name = "ruc", length = 11, nullable = false)
    private String value;

    protected Ruc() {
        // For JPA
    }

    public Ruc(String value) {
        if (!isValid(value)) {
            throw new InvalidRucException("RUC must have exactly 11 numeric digits");
        }
        this.value = value.trim();
    }

    private boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        var trimmed = value.trim();
        return RUC_PATTERN.matcher(trimmed).matches();
    }

    public String getNormalized() {
        return Objects.requireNonNull(value).trim();
    }
}
