package com.smartcart.verification.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Ruc(
        @Column(name = "ruc", nullable = false, length = 11)
        String value
) {
    private static final Pattern RUC_PATTERN = Pattern.compile("\\d{11}");

    // Constructor compacto de Java Records (Mantiene las reglas de validación del Dominio)
    public Ruc {
        if (value == null || !RUC_PATTERN.matcher(value.trim()).matches()) {
            throw new IllegalArgumentException("El RUC debe tener exactamente 11 dígitos numéricos");
        }
        value = value.trim();
    }

    // Constructor requerido por JPA / Hibernate para la deserialización orientada a objetos
    protected Ruc() {
        this(null);
    }

    // Método helper para obtener el String limpio cuando lo consuma el adaptador de infraestructura
    public String getNormalized() {
        return value;
    }
}