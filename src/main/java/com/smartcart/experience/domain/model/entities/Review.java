package com.smartcart.experience.domain.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    private String id;

    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Column(name = "buyer_id", nullable = false)
    private String buyerId;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "estado_publicacion")
    private String estadoPublicacion; // PUBLICADA, PENDIENTE, EN_REVISION

    @Column(name = "respuesta")
    private String respuesta;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_experience_id", nullable = false)
    private StoreExperience storeExperience;

    public Review() {}

    public Review(String id, String storeId, String buyerId, String comentario) {
        this.id = id;
        this.storeId = storeId;
        this.buyerId = buyerId;
        this.comentario = comentario;
        this.estadoPublicacion = "PENDIENTE";
        this.fechaCreacion = LocalDateTime.now();
    }

    public void publish() {
        this.estadoPublicacion = "PUBLICADA";
    }

    public void markForReview() {
        this.estadoPublicacion = "EN_REVISION";
    }

    public void addReply(String reply, String merchantId) {
        this.respuesta = reply;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void setStoreExperience(StoreExperience storeExperience) {
        this.storeExperience = storeExperience;
    }

    public String getId() { return id; }
    public String getStoreId() { return storeId; }
    public String getBuyerId() { return buyerId; }
    public String getComentario() { return comentario; }
    public String getEstadoPublicacion() { return estadoPublicacion; }
    public String getRespuesta() { return respuesta; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }
}