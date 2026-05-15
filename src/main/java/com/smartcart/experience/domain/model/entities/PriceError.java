package com.smartcart.experience.domain.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_errors")
public class PriceError {

    @Id
    private String id;

    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Column(name = "producto_id", nullable = false)
    private String productoId;

    @Column(name = "precio_digital", nullable = false)
    private Double precioDigital;

    @Column(name = "precio_fisico", nullable = false)
    private Double precioFisico;

    @Column(name = "discrepancia")
    private Double discrepancia;

    @Column(name = "estado_error")
    private String estadoError; // REPORTADO, CONFIRMADO, RECHAZADO

    @Column(name = "moneda")
    private String moneda;

    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_experience_id", nullable = false)
    private StoreExperience storeExperience;

    public PriceError() {}

    public PriceError(String id, String storeId, String productoId, Double precioDigital, Double precioFisico, String moneda) {
        this.id = id;
        this.storeId = storeId;
        this.productoId = productoId;
        this.precioDigital = precioDigital;
        this.precioFisico = precioFisico;
        this.moneda = moneda;
        this.discrepancia = precioFisico - precioDigital;
        this.estadoError = "REPORTADO";
        this.fechaReporte = LocalDateTime.now();
    }

    public void confirm() {
        this.estadoError = "CONFIRMADO";
    }

    public void reject() {
        this.estadoError = "RECHAZADO";
    }

    public void setStoreExperience(StoreExperience storeExperience) {
        this.storeExperience = storeExperience;
    }

    public String getId() { return id; }
    public String getStoreId() { return storeId; }
    public String getProductoId() { return productoId; }
    public Double getPrecioDigital() { return precioDigital; }
    public Double getPrecioFisico() { return precioFisico; }
    public Double getDiscrepancia() { return discrepancia; }
    public String getEstadoError() { return estadoError; }
    public String getMoneda() { return moneda; }
    public LocalDateTime getFechaReporte() { return fechaReporte; }
}