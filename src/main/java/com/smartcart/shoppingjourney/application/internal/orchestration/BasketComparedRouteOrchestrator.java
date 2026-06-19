package com.smartcart.shoppingjourney.application.internal.orchestration;

import com.smartcart.shared.infrastructure.events.BasketComparedIntegrationEvent;
import com.smartcart.shoppingjourney.domain.model.aggregates.ShoppingRoute;
import com.smartcart.shoppingjourney.domain.model.commands.CreateRouteCommand;
import com.smartcart.shoppingjourney.domain.model.commands.DefineResidenceCommand;
import com.smartcart.shoppingjourney.domain.model.commands.OptimizeRouteCommand;
import com.smartcart.shoppingjourney.domain.model.commands.RequestPathCommand;
import com.smartcart.shoppingjourney.domain.model.commands.SelectDestinationCommand;
import com.smartcart.shoppingjourney.domain.services.ShoppingJourneyCommandService;
import com.smartcart.shoppingjourney.infrastructure.acl.ShoppingPlanningACL;
import com.smartcart.shoppingjourney.infrastructure.persistence.jpa.repositories.ShoppingRouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasketComparedRouteOrchestrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasketComparedRouteOrchestrator.class);
    private static final List<String> ACTIVE_STATUSES = List.of(
            "CREATED", "OPTIMIZED", "PATH_REQUESTED", "NAVIGATING", "ARRIVED");

    private final ShoppingJourneyCommandService journeyCommandService;
    private final ShoppingPlanningACL planningACL;
    private final ShoppingRouteRepository routeRepository;

    public BasketComparedRouteOrchestrator(ShoppingJourneyCommandService journeyCommandService,
                                           ShoppingPlanningACL planningACL,
                                           ShoppingRouteRepository routeRepository) {
        this.journeyCommandService = journeyCommandService;
        this.planningACL = planningACL;
        this.routeRepository = routeRepository;
    }

    public OrchestrationResult orchestrate(BasketComparedIntegrationEvent event) {
        var snapshot = planningACL.translate(event);

        var existing = routeRepository.findFirstByBuyerIdAndListIdAndStatusInOrderByIdDesc(
                snapshot.buyerId(), snapshot.listId(), ACTIVE_STATUSES);
        ShoppingRoute route = existing.orElseGet(() ->
                journeyCommandService.handle(new CreateRouteCommand(snapshot.buyerId(), snapshot.listId())));
        final String routeId = route.getId();

        if (snapshot.hasResidence()) {
            journeyCommandService.handle(new DefineResidenceCommand(
                    routeId, snapshot.residenceLat(), snapshot.residenceLng()));
        }

        if (snapshot.rankedStoreIds().size() > 1 && snapshot.hasResidence()) {
            journeyCommandService.handle(new OptimizeRouteCommand(routeId, snapshot.rankedStoreIds()));
        } else {
            journeyCommandService.handle(new SelectDestinationCommand(routeId, snapshot.bestStoreId()));
        }

        boolean pathRequested = false;
        if (snapshot.hasResidence()) {
            route = journeyCommandService.handle(new RequestPathCommand(routeId));
            pathRequested = true;
            LOGGER.info("Política de Integración con Mapas: trayecto solicitado para ruta {}", routeId);
        } else {
            route = routeRepository.findById(routeId).orElse(route);
            LOGGER.info("Residencia no configurada en Planning; defina preferencias antes de navegar");
        }

        return new OrchestrationResult(route.getId(), pathRequested, route.getStatus());
    }

    public record OrchestrationResult(String routeId, boolean pathRequested, String routeStatus) {}
}
