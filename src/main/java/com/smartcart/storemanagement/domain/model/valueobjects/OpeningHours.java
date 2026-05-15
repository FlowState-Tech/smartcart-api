package com.smartcart.storemanagement.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
@Getter
@EqualsAndHashCode
public class OpeningHours {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    protected OpeningHours() {
        // For JPA
    }

    public OpeningHours(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week is required");
        }
        if (openTime == null || closeTime == null) {
            throw new IllegalArgumentException("Opening and closing times are required");
        }
        if (!closeTime.isAfter(openTime)) {
            throw new IllegalArgumentException("Closing time must be after opening time");
        }
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}

