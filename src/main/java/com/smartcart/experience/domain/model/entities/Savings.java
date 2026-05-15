package com.smartcart.experience.domain.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "savings")
public class Savings {

    @Id
    private String id;

    @Column(name = "recorrido_id", nullable = false)
    private String recorridoId;

    @Column(name = "buyer_id", nullable = false)
    private String buyerId;

    @Column(name = "monto_ahorrado")
    private Double montoAhorrado;

    @Column(name = "moneda")
    private String moneda;

    @Column(name = "precio_referencia")
    private Double precioReferencia;

    @Column(name = "precio_pagado")
    private Double precioPagado;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_experience_id", nullable = false)
    private StoreExperience storeExperience;

    public Savings() {}

    public Savings(String id, String recorridoId, String buyerId, Double precioReferencia, Double precioPagado, String moneda) {
        this.id = id;
        this.recorridoId = recorridoId;
        this.buyerId = buyerId;
        this.precioReferencia = precioReferencia;
        this.precioPagado = precioPagado;
        this.moneda = moneda;
        this.montoAhorrado = precioReferencia - precioPagado;
        this.fechaRegistro = LocalDateTime.now();
    }

    public boolean isPositiveSavings() {
        return montoAhorrado != null && montoAhorrado > 0;
    }

    public void setStoreExperience(StoreExperience storeExperience) {
        this.storeExperience = storeExperience;
    }

    public String getId() { return id; }
    public String getRecorridoId() { return recorridoId; }
    public String getBuyerId() { return buyerId; }
    public Double getMontoAhorrado() { return montoAhorrado; }
    public String getMoneda() { return moneda; }
    public Double getPrecioReferencia() { return precioReferencia; }
    public Double getPrecioPagado() { return precioPagado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}