package com.smartcart.experience.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ExperienceEventConsumer class
 * This class is used to consume events from other bounded contexts
 * Integration with Shopping Journey via REST or Message Broker
 */
@Service
public class ExperienceEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperienceEventConsumer.class);

    public ExperienceEventConsumer() {}

    public void handleRecorridoFinalizado(String recorridoId, String buyerId) {
        LOGGER.info("Received RecorridoFinalizado event for recorrido: {}, buyer: {}", recorridoId, buyerId);
        // TODO: Create StoreExperience and trigger feedback collection
    }
}