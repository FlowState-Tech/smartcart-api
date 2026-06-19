package com.smartcart.experience.application.internal.eventhandlers;

import com.smartcart.experience.domain.model.entities.StoreExperience;
import com.smartcart.experience.infrastructure.acl.ShoppingJourneyACL;
import com.smartcart.experience.infrastructure.persistence.jpa.repositories.StoreExperienceRepository;
import com.smartcart.shared.infrastructure.events.RecorridoFinalizadoIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
public class ExperienceEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperienceEventConsumer.class);

    private final StoreExperienceRepository repository;
    private final ShoppingJourneyACL journeyACL;

    public ExperienceEventConsumer(StoreExperienceRepository repository,
                                   ShoppingJourneyACL journeyACL) {
        this.repository = repository;
        this.journeyACL = journeyACL;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onRecorridoFinalizado(RecorridoFinalizadoIntegrationEvent event) {
        var snapshot = journeyACL.translate(event);
        LOGGER.info("RecorridoFinalizado → experience feedback for recorrido: {}, buyer: {}",
                snapshot.recorridoId(), snapshot.buyerId());
        if (!snapshot.hasStore()) return;
        repository.findByRecorridoId(snapshot.recorridoId()).orElseGet(() ->
                repository.save(new StoreExperience(
                        UUID.randomUUID().toString(),
                        snapshot.storeId(),
                        snapshot.buyerId(),
                        snapshot.recorridoId())));
    }
}
