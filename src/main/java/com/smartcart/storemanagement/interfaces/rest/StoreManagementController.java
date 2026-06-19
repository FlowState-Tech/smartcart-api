package com.smartcart.storemanagement.interfaces.rest;

import com.smartcart.storemanagement.domain.model.commands.ApplyProductClearanceCommand;
import com.smartcart.storemanagement.domain.model.commands.ProcessBulkInventoryCommand;
import com.smartcart.storemanagement.domain.model.queries.GetInventoryByStoreQuery;
import com.smartcart.storemanagement.domain.model.queries.GetStoreAnalyticsQuery;
import com.smartcart.storemanagement.domain.model.queries.GetStoreProfileQuery;
import com.smartcart.storemanagement.domain.services.InventoryCommandService;
import com.smartcart.storemanagement.domain.services.InventoryQueryService;
import com.smartcart.storemanagement.domain.services.StoreAnalyticsQueryService;
import com.smartcart.storemanagement.domain.services.StoreCommandService;
import com.smartcart.storemanagement.domain.services.StoreQueryService;
import com.smartcart.storemanagement.interfaces.rest.resources.BulkUploadResponse;
import com.smartcart.storemanagement.interfaces.rest.resources.ClearanceResponse;
import com.smartcart.storemanagement.interfaces.rest.resources.CreateClearanceRequest;
import com.smartcart.storemanagement.interfaces.rest.resources.CreateInventoryItemRequest;
import com.smartcart.storemanagement.interfaces.rest.resources.ProductStockResponse;
import com.smartcart.storemanagement.interfaces.rest.resources.RegisterStoreRequest;
import com.smartcart.storemanagement.interfaces.rest.resources.StoreAnalyticsResponse;
import com.smartcart.storemanagement.interfaces.rest.resources.StoreProfileResponse;
import com.smartcart.storemanagement.interfaces.rest.transform.InventoryBulkRecordsFromMultipartFileAssembler;
import com.smartcart.storemanagement.interfaces.rest.transform.RegisterStoreCommandFromResourceAssembler;
import com.smartcart.storemanagement.interfaces.rest.transform.ProductStockResponseFromEntityAssembler;
import com.smartcart.storemanagement.interfaces.rest.transform.StoreAnalyticsResponseFromReadModelAssembler;
import com.smartcart.storemanagement.interfaces.rest.transform.StoreProfileResponseFromEntityAssembler;
import com.smartcart.storemanagement.interfaces.rest.transform.AddInventoryItemCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/store-management", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Store Management", description = "Store Management Endpoints")
public class StoreManagementController {

    private final StoreCommandService storeCommandService;
    private final InventoryCommandService inventoryCommandService;
    private final StoreQueryService storeQueryService;
    private final InventoryQueryService inventoryQueryService;
    private final StoreAnalyticsQueryService analyticsQueryService;

    public StoreManagementController(StoreCommandService storeCommandService,
                                     InventoryCommandService inventoryCommandService,
                                     StoreQueryService storeQueryService,
                                     InventoryQueryService inventoryQueryService,
                                     StoreAnalyticsQueryService analyticsQueryService) {
        this.storeCommandService = storeCommandService;
        this.inventoryCommandService = inventoryCommandService;
        this.storeQueryService = storeQueryService;
        this.inventoryQueryService = inventoryQueryService;
        this.analyticsQueryService = analyticsQueryService;
    }

    @PostMapping("/stores")
    @Operation(summary = "Register store", description = "Registers a new store with merchant and branch data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.")})
    public ResponseEntity<Long> registerStore(@RequestBody RegisterStoreRequest request) {
        var command = RegisterStoreCommandFromResourceAssembler.toCommandFromResource(request);
        var store = storeCommandService.handle(command);
        if (store.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(store.get().getId(), HttpStatus.CREATED);
    }

    @GetMapping("/stores/{storeId}")
    @Operation(summary = "Get store profile", description = "Returns administrative and legal profile information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store profile found."),
            @ApiResponse(responseCode = "404", description = "Store not found.")})
    public ResponseEntity<StoreProfileResponse> getStoreProfile(@PathVariable Long storeId) {
        var store = storeQueryService.handle(new GetStoreProfileQuery(storeId));
        if (store.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var response = StoreProfileResponseFromEntityAssembler.toResourceFromEntity(store.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/stores/{storeId}/inventory/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Bulk inventory sync", description = "Processes a bulk inventory file for a store.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bulk sync completed."),
            @ApiResponse(responseCode = "400", description = "Bad request.")})
    public ResponseEntity<BulkUploadResponse> bulkInventorySync(@PathVariable Long storeId,
                                                                @RequestParam("file") MultipartFile file) {
        var records = InventoryBulkRecordsFromMultipartFileAssembler.toRecords(file);
        var command = new ProcessBulkInventoryCommand(storeId, records);
        var result = inventoryCommandService.handle(command);
        var response = new BulkUploadResponse(
                UUID.randomUUID().toString(),
                "COMPLETED",
                result.getProcessed(),
                result.getFailed(),
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stores/{storeId}/inventory/items")
    @Operation(summary = "Add inventory item", description = "Adds a single item to the inventory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item added successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.")})
    public ResponseEntity<ProductStockResponse> addInventoryItem(@PathVariable Long storeId,
                                                                 @RequestBody CreateInventoryItemRequest request) {
        var command = AddInventoryItemCommandFromResourceAssembler.toCommand(storeId, request);
        var result = inventoryCommandService.handle(command);
        var response = ProductStockResponseFromEntityAssembler.toResource(
                result.product(),
                result.priceItem(),
                result.stockPoint()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/stores/{storeId}/inventory/clearance")
    @Operation(summary = "Apply clearance", description = "Applies a clearance discount to a product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clearance applied."),
            @ApiResponse(responseCode = "400", description = "Bad request.")})
    public ResponseEntity<ClearanceResponse> applyClearance(@PathVariable Long storeId,
                                                           @RequestBody CreateClearanceRequest request) {
        var productId = parseProductId(request.productId());
        var command = new ApplyProductClearanceCommand(
                storeId,
                productId,
                request.discountPercentage(),
                request.expiryDate(),
                request.reason()
        );
        var result = inventoryCommandService.handle(command);
        var response = new ClearanceResponse(
                result.storeId(),
                result.sku(),
                result.discountPercentage(),
                result.expiryDate(),
                "APPLIED"
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stores/{storeId}/inventory")
    @Operation(summary = "Get inventory", description = "Returns product catalog with stock and prices.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved."),
            @ApiResponse(responseCode = "404", description = "Inventory not found.")})
    public ResponseEntity<List<ProductStockResponse>> getInventory(@PathVariable Long storeId,
                                                                   @RequestParam(required = false) Long category,
                                                                   @RequestParam(required = false) String sku) {
        var inventory = inventoryQueryService.handle(new GetInventoryByStoreQuery(storeId, category, sku));
        if (inventory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var normalizedSku = sku == null ? null : sku.trim();
        var responses = inventory.get().getProducts().stream()
                .filter(product -> category == null || category.equals(product.getCategoryId()))
                .filter(product -> normalizedSku == null || normalizedSku.equalsIgnoreCase(product.getSku().getCode()))
                .map(product -> ProductStockResponseFromEntityAssembler.toResource(
                        product,
                        inventory.get().findPriceItem(product.getSku()).orElse(null),
                        inventory.get().findStockPoint(product.getSku()).orElse(null)
                ))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/stores/{storeId}/analytics")
    @Operation(summary = "Get analytics", description = "Returns store analytics metrics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics retrieved."),
            @ApiResponse(responseCode = "404", description = "Analytics not found.")})
    public ResponseEntity<StoreAnalyticsResponse> getAnalytics(@PathVariable Long storeId) {
        var analytics = analyticsQueryService.handle(new GetStoreAnalyticsQuery(storeId));
        if (analytics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var response = StoreAnalyticsResponseFromReadModelAssembler.toResource(analytics.get());
        return ResponseEntity.ok(response);
    }

    private Long parseProductId(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Product id is required");
        }
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Product id must be numeric", ex);
        }
    }
}
