package com.smartcart.shoppingplanning.interfaces.rest;

import com.smartcart.shoppingplanning.domain.model.commands.AddItemToListCommand;
import com.smartcart.shoppingplanning.domain.model.commands.ApplyFamilyBasketCommand;
import com.smartcart.shoppingplanning.domain.model.commands.CreateShoppingListCommand;
import com.smartcart.shoppingplanning.domain.model.commands.RemoveItemFromListCommand;
import com.smartcart.shoppingplanning.domain.model.queries.GetShoppingListsByBuyerQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetShoppingListQuery;
import com.smartcart.shoppingplanning.domain.services.shoppinglist.ShoppingListCommandService;
import com.smartcart.shoppingplanning.domain.services.shoppinglist.ShoppingListQueryService;
import com.smartcart.shoppingplanning.interfaces.rest.resources.AddItemRequest;
import com.smartcart.shoppingplanning.interfaces.rest.resources.CreateListRequest;
import com.smartcart.shoppingplanning.interfaces.rest.resources.ShoppingListResponse;
import com.smartcart.shoppingplanning.interfaces.rest.transform.ShoppingPlanningResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/planning", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "ShoppingList", description = "Shopping Planning — lista de compras")
public class ShoppingListController {

    private final ShoppingListCommandService commandService;
    private final ShoppingListQueryService queryService;

    public ShoppingListController(ShoppingListCommandService commandService,
                                    ShoppingListQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping("/lists")
    @Operation(summary = "Crear lista de compras")
    public ResponseEntity<ShoppingListResponse> createList(@Valid @RequestBody CreateListRequest body) {
        var list = commandService.handle(new CreateShoppingListCommand(body.buyerId(), body.name()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingPlanningResourceAssembler.toListResponse(list));
    }

    @GetMapping("/lists/{listId}")
    @Operation(summary = "Obtener lista de compras")
    public ResponseEntity<ShoppingListResponse> getList(@PathVariable Long listId) {
        return queryService.handle(new GetShoppingListQuery(listId))
                .map(l -> ResponseEntity.ok(ShoppingPlanningResourceAssembler.toListResponse(l)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buyers/{buyerId}/lists")
    @Operation(summary = "Listar canastas por comprador")
    public ResponseEntity<List<ShoppingListResponse>> listByBuyer(@PathVariable Long buyerId) {
        var lists = queryService.handle(new GetShoppingListsByBuyerQuery(buyerId));
        return ResponseEntity.ok(lists.stream().map(ShoppingPlanningResourceAssembler::toListResponse).toList());
    }

    @PostMapping("/lists/{listId}/items")
    @Operation(summary = "Añadir productos a la canasta")
    public ResponseEntity<ShoppingListResponse> addItem(@PathVariable Long listId,
                                                        @Valid @RequestBody AddItemRequest body) {
        var list = commandService.handle(new AddItemToListCommand(
                listId, body.sku(), body.productName(), body.quantity(), body.unit()));
        return ResponseEntity.ok(ShoppingPlanningResourceAssembler.toListResponse(list));
    }

    @DeleteMapping("/lists/{listId}/items/{itemId}")
    @Operation(summary = "Eliminar productos de la canasta")
    public ResponseEntity<ShoppingListResponse> removeItem(@PathVariable Long listId, @PathVariable Long itemId) {
        var list = commandService.handle(new RemoveItemFromListCommand(listId, itemId));
        return ResponseEntity.ok(ShoppingPlanningResourceAssembler.toListResponse(list));
    }

    @PostMapping("/buyers/{buyerId}/lists/{listId}/apply-family-basket")
    @Operation(summary = "Aplicar canasta básica familiar")
    public ResponseEntity<ShoppingListResponse> applyFamilyBasket(@PathVariable Long buyerId,
                                                                   @PathVariable Long listId) {
        var list = commandService.handle(new ApplyFamilyBasketCommand(buyerId, listId));
        return ResponseEntity.ok(ShoppingPlanningResourceAssembler.toListResponse(list));
    }
}
