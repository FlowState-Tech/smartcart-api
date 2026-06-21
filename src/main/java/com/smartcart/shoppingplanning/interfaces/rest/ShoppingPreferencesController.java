package com.smartcart.shoppingplanning.interfaces.rest;

import com.smartcart.shoppingplanning.domain.model.commands.ConfigureFamilyBasketCommand;
import com.smartcart.shoppingplanning.domain.model.commands.DefineBudgetCommand;
import com.smartcart.shoppingplanning.domain.model.commands.DefineResidencePreferenceCommand;
import com.smartcart.shoppingplanning.domain.model.commands.SelectPreferredStoresCommand;
import com.smartcart.shoppingplanning.domain.model.queries.GetPreferencesQuery;
import com.smartcart.shoppingplanning.domain.services.preferences.ShoppingPreferencesCommandService;
import com.smartcart.shoppingplanning.domain.services.preferences.ShoppingPreferencesQueryService;
import com.smartcart.shoppingplanning.interfaces.rest.resources.PreferencesRequest;
import com.smartcart.shoppingplanning.interfaces.rest.resources.PreferencesResponse;
import com.smartcart.shoppingplanning.interfaces.rest.transform.ShoppingPlanningResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/planning", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Shopping Preferences", description = "Shopping Planning — preferencias del consumidor")
public class ShoppingPreferencesController {

    private final ShoppingPreferencesCommandService commandService;
    private final ShoppingPreferencesQueryService queryService;

    public ShoppingPreferencesController(ShoppingPreferencesCommandService commandService,
                                           ShoppingPreferencesQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PutMapping("/preferences/{buyerId}")
    @Operation(summary = "Configurar canasta familiar, tiendas, presupuesto y residencia")
    public ResponseEntity<PreferencesResponse> updatePreferences(@PathVariable Long buyerId,
                                                                 @Valid @RequestBody PreferencesRequest body) {
        if (body.familyBasketSkus() != null) {
            commandService.handle(new ConfigureFamilyBasketCommand(buyerId, body.familyBasketSkus()));
        }
        if (body.preferredStoreIds() != null) {
            commandService.handle(new SelectPreferredStoresCommand(buyerId, body.preferredStoreIds()));
        }
        if (body.budgetAmount() != null) {
            commandService.handle(new DefineBudgetCommand(buyerId, body.budgetAmount(), body.budgetCurrency()));
        }
        if (body.residenceLat() != null && body.residenceLng() != null) {
            commandService.handle(new DefineResidencePreferenceCommand(buyerId, body.residenceLat(), body.residenceLng()));
        }
        return queryService.handle(new GetPreferencesQuery(buyerId))
                .map(p -> ResponseEntity.ok(ShoppingPlanningResourceAssembler.toPrefsResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/preferences/{buyerId}")
    @Operation(summary = "Obtener preferencias de compra")
    public ResponseEntity<PreferencesResponse> getPreferences(@PathVariable Long buyerId) {
        return queryService.handle(new GetPreferencesQuery(buyerId))
                .map(p -> ResponseEntity.ok(ShoppingPlanningResourceAssembler.toPrefsResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }
}
