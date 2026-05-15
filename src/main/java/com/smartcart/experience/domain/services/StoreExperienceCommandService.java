package com.smartcart.experience.domain.services;

import com.smartcart.experience.domain.model.commands.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreExperienceCommandService {

    public Optional<Object> handle(RateStoreCommand command) {
        // TODO: Implement - Create rating entity and publish TiendaCalificada event
        return Optional.of(new Object());
    }

    public Optional<Object> handle(PostReviewCommand command) {
        // TODO: Implement - Create review, validate content, publish ReseñaPublicada or ReseñaMarcadaParaRevision
        return Optional.of(new Object());
    }

    public Optional<Object> handle(ReportPriceErrorCommand command) {
        // TODO: Implement - Create price error and publish ErrorDePrecioReportado event
        return Optional.of(new Object());
    }

    public Optional<Object> handle(CalculateSavingsCommand command) {
        // TODO: Implement - Calculate savings and publish AhorroTotalCalculado event
        return Optional.of(new Object());
    }

    public Optional<Object> handle(ReplyReviewCommand command) {
        // TODO: Implement - Add reply to review
        return Optional.of(new Object());
    }

    public Optional<Object> handle(ConfirmPriceErrorCommand command) {
        // TODO: Implement - Update price error status and publish ErrorDePrecioConfirmado if confirmed
        return Optional.of(new Object());
    }
}