package com.smartcart.shoppingjourney.application.internal.eventhandlers;

import com.smartcart.shared.infrastructure.events.BasketComparedIntegrationEvent;
import com.smartcart.shoppingjourney.application.internal.orchestration.BasketComparedRouteOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Canasta Comparada → Solicitar trayecto (Política de Integración con Mapas).
 * Reacciona en Shopping Journey; Planning solo publica el evento.
 */
@Component
public class BasketComparedJourneyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasketComparedJourneyEventHandler.class);

    private final BasketComparedRouteOrchestrator orchestrator;

    public BasketComparedJourneyEventHandler(BasketComparedRouteOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onBasketCompared(BasketComparedIntegrationEvent event) {
        LOGGER.info("Canasta Comparada → Journey: buyer={} list={} store={} ranked={}",
                event.buyerId(), event.listId(), event.bestStoreId(), event.rankedStoreIds());
        var result = orchestrator.orchestrate(event);
        LOGGER.info("Solicitar trayecto: routeId={} pathRequested={} status={}",
                result.routeId(), result.pathRequested(), result.routeStatus());
    }
}
