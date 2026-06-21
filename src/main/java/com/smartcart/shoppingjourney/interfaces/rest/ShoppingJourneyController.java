package com.smartcart.shoppingjourney.interfaces.rest;

import com.smartcart.shoppingjourney.domain.model.commands.CreateRouteCommand;
import com.smartcart.shoppingjourney.domain.model.commands.DefineResidenceCommand;
import com.smartcart.shoppingjourney.domain.model.commands.FinishJourneyCommand;
import com.smartcart.shoppingjourney.domain.model.commands.OptimizeRouteCommand;
import com.smartcart.shoppingjourney.domain.model.commands.RegisterArrivalCommand;
import com.smartcart.shoppingjourney.domain.model.commands.RequestPathCommand;
import com.smartcart.shoppingjourney.domain.model.commands.SelectDestinationCommand;
import com.smartcart.shoppingjourney.domain.model.commands.StartNavigationCommand;
import com.smartcart.shoppingjourney.domain.model.queries.FindRoutesQuery;
import com.smartcart.shoppingjourney.domain.model.queries.GetRouteQuery;
import com.smartcart.shoppingjourney.domain.services.ShoppingJourneyCommandService;
import com.smartcart.shoppingjourney.domain.services.ShoppingJourneyQueryService;
import com.smartcart.shoppingjourney.interfaces.rest.resources.CreateRouteRequest;
import com.smartcart.shoppingjourney.interfaces.rest.resources.DefineResidenceRequest;
import com.smartcart.shoppingjourney.interfaces.rest.resources.OptimalRouteViewResponse;
import com.smartcart.shoppingjourney.interfaces.rest.resources.OptimizeRouteRequest;
import com.smartcart.shoppingjourney.interfaces.rest.resources.RegisterArrivalRequest;
import com.smartcart.shoppingjourney.interfaces.rest.resources.RouteResponse;
import com.smartcart.shoppingjourney.interfaces.rest.resources.RouteStopResponse;
import com.smartcart.shoppingjourney.interfaces.rest.resources.SelectDestinationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/journey", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Shopping Journey", description = "Route planning, Maps policy and navigation")
public class ShoppingJourneyController {

    private final ShoppingJourneyCommandService commandService;
    private final ShoppingJourneyQueryService queryService;

    public ShoppingJourneyController(ShoppingJourneyCommandService commandService,
                                     ShoppingJourneyQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping("/routes")
    @Operation(summary = "Create shopping route")
    public ResponseEntity<RouteResponse> createRoute(@RequestBody CreateRouteRequest body) {
        var route = commandService.handle(new CreateRouteCommand(body.buyerId(), body.listId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(route));
    }

    @GetMapping("/routes")
    @Operation(summary = "Find routes by buyer and optional list")
    public ResponseEntity<List<RouteResponse>> findRoutes(@RequestParam Long buyerId,
                                                          @RequestParam(required = false) Long listId) {
        var routes = queryService.handle(new FindRoutesQuery(buyerId, listId));
        return ResponseEntity.ok(routes.stream().map(this::toResponse).toList());
    }

    @GetMapping("/routes/{routeId}")
    @Operation(summary = "Get route details")
    public ResponseEntity<RouteResponse> getRoute(@PathVariable String routeId) {
        return queryService.handle(new GetRouteQuery(routeId))
                .map(r -> ResponseEntity.ok(toResponse(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/routes/{routeId}/optimal-view")
    @Operation(summary = "Vista de Ruta Óptima (Google Maps)")
    public ResponseEntity<OptimalRouteViewResponse> optimalView(@PathVariable String routeId) {
        return queryService.handle(new GetRouteQuery(routeId))
                .map(r -> ResponseEntity.ok(new OptimalRouteViewResponse(
                        r.getId(), r.getStatus(), r.getEncodedPolyline(),
                        r.getDistanceMeters(), r.getDurationSeconds(), r.getMapsProvider(),
                        r.getStops().stream()
                                .map(s -> new RouteStopResponse(s.storeId(), s.storeName(), s.latitude(), s.longitude(), s.sequence()))
                                .toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/routes/{routeId}/residence")
    @Operation(summary = "Definir ubicación de residencia")
    public ResponseEntity<RouteResponse> defineResidence(@PathVariable String routeId,
                                                         @RequestBody DefineResidenceRequest body) {
        var route = commandService.handle(new DefineResidenceCommand(routeId, body.latitude(), body.longitude()));
        return ResponseEntity.ok(toResponse(route));
    }

    @PostMapping("/routes/{routeId}/destination")
    @Operation(summary = "Seleccionar tienda de destino")
    public ResponseEntity<RouteResponse> selectDestination(@PathVariable String routeId,
                                                           @RequestBody SelectDestinationRequest body) {
        var route = commandService.handle(new SelectDestinationCommand(routeId, body.storeId()));
        return ResponseEntity.ok(toResponse(route));
    }

    @PostMapping("/routes/{routeId}/optimize")
    @Operation(summary = "Optimizar ruta de compra (multi-parada)")
    public ResponseEntity<RouteResponse> optimizeRoute(@PathVariable String routeId,
                                                       @Valid @RequestBody OptimizeRouteRequest body) {
        var route = commandService.handle(new OptimizeRouteCommand(routeId, body.storeIds()));
        return ResponseEntity.ok(toResponse(route));
    }

    @PostMapping("/routes/{routeId}/request-path")
    @Operation(summary = "Solicitar trayecto — Política de Integración con Mapas")
    public ResponseEntity<RouteResponse> requestPath(@PathVariable String routeId) {
        var route = commandService.handle(new RequestPathCommand(routeId));
        return ResponseEntity.ok(toResponse(route));
    }

    @PostMapping("/routes/{routeId}/start-navigation")
    @Operation(summary = "Iniciar navegación")
    public ResponseEntity<RouteResponse> startNavigation(@PathVariable String routeId) {
        var route = commandService.handle(new StartNavigationCommand(routeId));
        return ResponseEntity.ok(toResponse(route));
    }

    @PostMapping("/routes/{routeId}/register-arrival")
    @Operation(summary = "Registrar llegada a local (geofencing GPS)")
    public ResponseEntity<RouteResponse> registerArrival(@PathVariable String routeId,
                                                         @Valid @RequestBody RegisterArrivalRequest body) {
        var route = commandService.handle(new RegisterArrivalCommand(routeId, body.latitude(), body.longitude()));
        return ResponseEntity.ok(toResponse(route));
    }

    @PostMapping("/routes/{routeId}/finish")
    @Operation(summary = "Finalizar recorrido")
    public ResponseEntity<RouteResponse> finish(@PathVariable String routeId) {
        var route = commandService.handle(new FinishJourneyCommand(routeId));
        return ResponseEntity.ok(toResponse(route));
    }

    private RouteResponse toResponse(com.smartcart.shoppingjourney.domain.model.aggregates.ShoppingRoute route) {
        var stops = route.getStops().stream()
                .map(s -> new RouteStopResponse(s.storeId(), s.storeName(), s.latitude(), s.longitude(), s.sequence()))
                .toList();
        return new RouteResponse(route.getId(), route.getBuyerId(), route.getListId(),
                route.getResidenceLat(), route.getResidenceLng(), route.getDestinationStoreId(),
                route.getStatus(), route.getEncodedPolyline(), route.getDistanceMeters(),
                route.getDurationSeconds(), route.getMapsProvider(), stops);
    }
}
