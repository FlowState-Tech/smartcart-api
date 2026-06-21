package com.smartcart.shoppingjourney.application.internal.commandservices;

import com.smartcart.shared.domain.exceptions.ResourceNotFoundException;
import com.smartcart.shared.infrastructure.events.RecorridoFinalizadoIntegrationEvent;
import com.smartcart.shoppingjourney.domain.events.NavigationToStoreStartedEvent;
import com.smartcart.shoppingjourney.domain.events.RouteSearchedEvent;
import com.smartcart.shoppingjourney.domain.events.ShoppingRouteCompletedEvent;
import com.smartcart.shoppingjourney.domain.events.StoreVisitConfirmedEvent;
import com.smartcart.shoppingjourney.domain.model.aggregates.ShoppingRoute;
import com.smartcart.shoppingjourney.domain.model.commands.CreateRouteCommand;
import com.smartcart.shoppingjourney.domain.model.commands.DefineResidenceCommand;
import com.smartcart.shoppingjourney.domain.model.commands.FinishJourneyCommand;
import com.smartcart.shoppingjourney.domain.model.commands.OptimizeRouteCommand;
import com.smartcart.shoppingjourney.domain.model.commands.RegisterArrivalCommand;
import com.smartcart.shoppingjourney.domain.model.commands.RequestPathCommand;
import com.smartcart.shoppingjourney.domain.model.commands.SelectDestinationCommand;
import com.smartcart.shoppingjourney.domain.model.commands.StartNavigationCommand;
import com.smartcart.shoppingjourney.domain.model.valueobjects.RouteStop;
import com.smartcart.shoppingjourney.domain.services.GeofencingService;
import com.smartcart.shoppingjourney.domain.services.MapsIntegrationPolicy;
import com.smartcart.shoppingjourney.domain.services.RouteOptimizationService;
import com.smartcart.shoppingjourney.domain.services.ShoppingJourneyCommandService;
import com.smartcart.shoppingjourney.domain.services.StoreHoursService;
import com.smartcart.shoppingjourney.infrastructure.persistence.jpa.repositories.ShoppingRouteRepository;
import com.smartcart.storemanagement.domain.model.aggregates.Store;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingJourneyCommandServiceImpl implements ShoppingJourneyCommandService {

    private final ShoppingRouteRepository routeRepository;
    private final StoreRepository storeRepository;
    private final MapsIntegrationPolicy mapsPolicy;
    private final RouteOptimizationService optimizationService;
    private final GeofencingService geofencingService;
    private final StoreHoursService storeHoursService;
    private final ApplicationEventPublisher eventPublisher;

    public ShoppingJourneyCommandServiceImpl(ShoppingRouteRepository routeRepository,
                                               StoreRepository storeRepository,
                                               MapsIntegrationPolicy mapsPolicy,
                                               RouteOptimizationService optimizationService,
                                               GeofencingService geofencingService,
                                               StoreHoursService storeHoursService,
                                               ApplicationEventPublisher eventPublisher) {
        this.routeRepository = routeRepository;
        this.storeRepository = storeRepository;
        this.mapsPolicy = mapsPolicy;
        this.optimizationService = optimizationService;
        this.geofencingService = geofencingService;
        this.storeHoursService = storeHoursService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ShoppingRoute handle(CreateRouteCommand command) {
        return routeRepository.save(new ShoppingRoute(command.buyerId(), command.listId()));
    }

    @Override
    @Transactional
    public ShoppingRoute handle(DefineResidenceCommand command) {
        var route = findRoute(command.routeId());
        route.defineResidence(command.latitude(), command.longitude());
        return routeRepository.save(route);
    }

    @Override
    @Transactional
    public ShoppingRoute handle(SelectDestinationCommand command) {
        var route = findRoute(command.routeId());
        var storeId = resolveOpenStore(command.storeId());
        var coords = storeCoordinates(storeId);
        route.selectDestination(storeId, coords[0], coords[1]);
        return routeRepository.save(route);
    }

    @Override
    @Transactional
    public ShoppingRoute handle(OptimizeRouteCommand command) {
        var route = findRoute(command.routeId());
        if (route.getResidenceLat() == null) {
            throw new IllegalStateException("Define residence before optimizing route");
        }
        if (command.storeIds() == null || command.storeIds().isEmpty()) {
            throw new IllegalArgumentException("At least one store id is required");
        }
        var stops = new ArrayList<RouteStop>();
        for (Long storeId : command.storeIds()) {
            var openId = resolveOpenStore(storeId);
            var store = storeRepository.findById(openId)
                    .orElseThrow(() -> new ResourceNotFoundException("Store", openId));
            var branch = requireBranch(store);
            stops.add(new RouteStop(openId, store.getName(),
                    branch.getAddress().getLatitude(), branch.getAddress().getLongitude(), 0));
        }
        var optimized = optimizationService.optimize(
                route.getResidenceLat(), route.getResidenceLng(), stops);
        route.setOptimizedStops(optimized);
        return routeRepository.save(route);
    }

    @Override
    @Transactional
    public ShoppingRoute handle(RequestPathCommand command) {
        var route = findRoute(command.routeId());
        if (route.getResidenceLat() == null || route.getDestinationLat() == null) {
            throw new IllegalStateException("Define residence and destination before requesting path");
        }
        var path = mapsPolicy.requestPath(route);
        route.applyPath(path.encodedPolyline(), path.distanceMeters(), path.durationSeconds(), path.provider());
        var saved = routeRepository.save(route);
        eventPublisher.publishEvent(new RouteSearchedEvent(
                saved.getId(), saved.getResidenceLat(), saved.getResidenceLng(), saved.getDestinationStoreId()));
        return saved;
    }

    @Override
    @Transactional
    public ShoppingRoute handle(StartNavigationCommand command) {
        var route = findRoute(command.routeId());
        route.startNavigation();
        var saved = routeRepository.save(route);
        eventPublisher.publishEvent(new NavigationToStoreStartedEvent(saved.getId(), saved.getBuyerId()));
        return saved;
    }

    @Override
    @Transactional
    public ShoppingRoute handle(RegisterArrivalCommand command) {
        var route = findRoute(command.routeId());
        var target = route.nextArrivalTarget();
        if (!geofencingService.isWithinStoreRadius(
                command.latitude(), command.longitude(), target[0], target[1])) {
            throw new IllegalArgumentException("GPS location too far from store (geofencing)");
        }
        var stopIndex = route.getCurrentStopIndex();
        var storeId = resolveArrivalStoreId(route);
        route.registerArrival();
        var saved = routeRepository.save(route);
        eventPublisher.publishEvent(new StoreVisitConfirmedEvent(saved.getId(), storeId, stopIndex));
        return saved;
    }

    @Override
    @Transactional
    public ShoppingRoute handle(FinishJourneyCommand command) {
        var route = findRoute(command.routeId());
        route.finish();
        var saved = routeRepository.save(route);
        eventPublisher.publishEvent(new ShoppingRouteCompletedEvent(
                saved.getId(), saved.getBuyerId(), saved.getDestinationStoreId(), saved.getListId()));
        eventPublisher.publishEvent(new RecorridoFinalizadoIntegrationEvent(
                saved.getId(), String.valueOf(saved.getBuyerId()),
                saved.getDestinationStoreId(), saved.getListId()));
        return saved;
    }

    private Long resolveArrivalStoreId(ShoppingRoute route) {
        if (!route.getStops().isEmpty() && route.getCurrentStopIndex() < route.getOrderedStops().size()) {
            return route.getOrderedStops().get(route.getCurrentStopIndex()).storeId();
        }
        return route.getDestinationStoreId();
    }

    private Long resolveOpenStore(Long storeId) {
        if (storeHoursService.isStoreOpen(storeId)) {
            return storeId;
        }
        return storeHoursService.findNearestOpen24hAlternative(storeId).orElse(storeId);
    }

    private ShoppingRoute findRoute(String routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingRoute", routeId));
    }

    private double[] storeCoordinates(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", storeId));
        var branch = requireBranch(store);
        return new double[]{branch.getAddress().getLatitude(), branch.getAddress().getLongitude()};
    }

    private com.smartcart.storemanagement.domain.model.entities.StoreBranch requireBranch(Store store) {
        if (store.getBranches() == null || store.getBranches().isEmpty()) {
            throw new IllegalArgumentException("Store has no branches: " + store.getId());
        }
        return store.getBranches().getFirst();
    }
}
