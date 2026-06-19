package com.smartcart.shoppingplanning.interfaces.rest;

import com.smartcart.shoppingplanning.domain.model.commands.CompareBasketCommand;
import com.smartcart.shoppingplanning.domain.model.queries.GetPriceProjectionsQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetSubstitutesQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetTotalCostQuery;
import com.smartcart.shoppingplanning.domain.model.queries.LookupBarcodeQuery;
import com.smartcart.shoppingplanning.domain.model.queries.VerifyStockQuery;
import com.smartcart.shoppingplanning.domain.services.pricecomparison.PriceComparisonCommandService;
import com.smartcart.shoppingplanning.domain.services.pricecomparison.PriceComparisonQueryService;
import com.smartcart.shoppingplanning.interfaces.rest.resources.BarcodeLookupResponse;
import com.smartcart.shoppingplanning.interfaces.rest.resources.CompareBasketResponse;
import com.smartcart.shoppingplanning.interfaces.rest.resources.PriceComparisonResponse;
import com.smartcart.shoppingplanning.interfaces.rest.resources.SubstituteResponse;
import com.smartcart.shoppingplanning.interfaces.rest.resources.TotalCostResponse;
import com.smartcart.shoppingplanning.interfaces.rest.transform.ShoppingPlanningResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/planning", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "PriceComparison", description = "Shopping Planning — comparación de precios y stock")
public class PriceComparisonController {

    private final PriceComparisonCommandService commandService;
    private final PriceComparisonQueryService queryService;

    public PriceComparisonController(PriceComparisonCommandService commandService,
                                     PriceComparisonQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping("/lists/{listId}/compare-prices")
    @Operation(summary = "Comparar precios entre tiendas")
    public ResponseEntity<List<PriceComparisonResponse>> comparePrices(
            @PathVariable Long listId,
            @RequestParam(required = false) String storeFormat) {
        var results = queryService.handle(new GetPriceProjectionsQuery(listId, storeFormat));
        return ResponseEntity.ok(results.stream().map(ShoppingPlanningResourceAssembler::toComparison).toList());
    }

    @GetMapping("/lists/{listId}/total-cost")
    @Operation(summary = "Requerir costo total de canasta")
    public ResponseEntity<TotalCostResponse> totalCost(@PathVariable Long listId) {
        var total = queryService.handle(new GetTotalCostQuery(listId)).orElse(BigDecimal.ZERO);
        return ResponseEntity.ok(new TotalCostResponse(listId, total, "PEN"));
    }

    @PostMapping("/lists/{listId}/compare-basket")
    @Operation(summary = "Comparar canasta — emite Canasta Comparada hacia Shopping Journey")
    public ResponseEntity<CompareBasketResponse> compareBasket(@PathVariable Long listId) {
        var result = commandService.handle(new CompareBasketCommand(listId));
        return ResponseEntity.ok(new CompareBasketResponse(
                result.comparisons().stream().map(ShoppingPlanningResourceAssembler::toComparison).toList(),
                result.canastaComparadaEmitted(), result.journeyRoutesEndpoint()));
    }

    @GetMapping("/lists/{listId}/stores/{storeId}/stock")
    @Operation(summary = "Verificar stock de producto")
    public ResponseEntity<List<String>> verifyStock(@PathVariable Long listId, @PathVariable Long storeId) {
        return ResponseEntity.ok(queryService.handle(new VerifyStockQuery(listId, storeId)));
    }

    @GetMapping("/lists/{listId}/stores/{storeId}/substitutes")
    @Operation(summary = "Política de Sugerencia de Sustitutos")
    public ResponseEntity<SubstituteResponse> substitutes(@PathVariable Long listId,
                                                          @PathVariable Long storeId,
                                                          @RequestParam String sku) {
        return queryService.handle(new GetSubstitutesQuery(listId, storeId, sku))
                .map(s -> ResponseEntity.ok(new SubstituteResponse(
                        s.originalSku(), s.substituteSku(), s.substituteName(), s.storeId(), s.reason())))
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/lists/{listId}/stores/{storeId}/substitutes/all")
    @Operation(summary = "Sustitutos para todos los productos agotados")
    public ResponseEntity<List<SubstituteResponse>> allSubstitutes(@PathVariable Long listId,
                                                                   @PathVariable Long storeId) {
        var subs = queryService.handleAllSubstitutes(new VerifyStockQuery(listId, storeId));
        return ResponseEntity.ok(subs.stream()
                .map(s -> new SubstituteResponse(s.originalSku(), s.substituteSku(), s.substituteName(), s.storeId(), s.reason()))
                .toList());
    }

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Consultar catálogo por código de barras")
    public ResponseEntity<List<BarcodeLookupResponse>> lookupBarcode(@PathVariable String barcode) {
        var results = queryService.handle(new LookupBarcodeQuery(barcode));
        return ResponseEntity.ok(results.stream()
                .map(r -> new BarcodeLookupResponse(r.sku(), r.productName(), r.storeId(), r.storeName(), r.price()))
                .toList());
    }
}
