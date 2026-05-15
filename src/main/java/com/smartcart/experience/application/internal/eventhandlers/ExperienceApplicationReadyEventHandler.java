package com.smartcart.experience.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * ExperienceApplicationReadyEventHandler class
 * This class is used to handle the ApplicationReadyEvent
 */
@Service
public class ExperienceApplicationReadyEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperienceApplicationReadyEventHandler.class);

    public ExperienceApplicationReadyEventHandler() {}

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Experience bounded context initialized for {} at {}", applicationName, currentTimestamp());
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}