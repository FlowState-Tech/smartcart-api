package com.smartcart.experience.domain.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    private String id;

    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Column(name = "buyer_id", nullable = false)
    private String buyerId;

    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_experience_id", nullable = false)
    private StoreExperience storeExperience;

    public Rating() {}

    public Rating(String id, String storeId, String buyerId, Integer puntuacion) {
        this.id = id;
        this.storeId = storeId;
        this.buyerId = buyerId;
        this.puntuacion = puntuacion;
        this.fechaRegistro = LocalDateTime.now();
    }

    public void setStoreExperience(StoreExperience storeExperience) {
        this.storeExperience = storeExperience;
    }

    public String getId() { return id; }
    public String getStoreId() { return storeId; }
    public String getBuyerId() { return buyerId; }
    public Integer getPuntuacion() { return puntuacion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}