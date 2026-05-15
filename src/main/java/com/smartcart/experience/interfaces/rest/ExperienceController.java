package com.smartcart.experience.interfaces.rest;

import com.smartcart.experience.domain.model.commands.*;
import com.smartcart.experience.domain.model.queries.*;
import com.smartcart.experience.domain.services.*;
import com.smartcart.experience.interfaces.rest.resources.*;
import com.smartcart.experience.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/experience", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Experience", description = "Post-purchase experience endpoints for ratings, reviews, and savings")
public class ExperienceController {

    private final StoreExperienceCommandService commandService;
    private final StoreExperienceQueryService queryService;

    public ExperienceController(StoreExperienceCommandService commandService,
                                StoreExperienceQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    // ================== CONSUMER ENDPOINTS ==================

    @PostMapping("/stores/{storeId}/ratings")
    @Operation(summary = "Rate a store (Consumer)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rating created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<RatingResponse> rateStore(
            @PathVariable String storeId,
            @Valid @RequestBody RateStoreResource body) {
        var command = RateStoreCommandFromResourceAssembler.toCommandFromResource(storeId, body);
        var result = commandService.handle(command).orElseThrow();
        return ResponseEntity.ok(RatingResponseFromEntityAssembler.toResourceFromEntity(result));
    }

    @PostMapping("/stores/{storeId}/reviews")
    @Operation(summary = "Post a review (Consumer)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review posted"),
            @ApiResponse(responseCode = "400", description = "Invalid input or content violation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ReviewResponse> postReview(
            @PathVariable String storeId,
            @Valid @RequestBody PostReviewResource body) {
        var command = PostReviewCommandFromResourceAssembler.toCommandFromResource(storeId, body);
        var result = commandService.handle(command).orElseThrow();
        return ResponseEntity.ok(ReviewResponseFromEntityAssembler.toResourceFromEntity(result));
    }

    @GetMapping("/stores/{storeId}/reviews")
    @Operation(summary = "Get published reviews for a store (Consumer)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reviews retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ReviewResponse>> getPublishedReviews(
            @PathVariable String storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var query = new GetPublishedReviewsQuery(storeId, page, size);
        var result = queryService.handle(query);
        return ResponseEntity.ok(result.stream()
                .map(ReviewResponseFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @PostMapping("/stores/{storeId}/price-errors")
    @Operation(summary = "Report a price error (Consumer)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Price error reported"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PriceErrorResponse> reportPriceError(
            @PathVariable String storeId,
            @Valid @RequestBody ReportPriceErrorResource body) {
        var command = ReportPriceErrorCommandFromResourceAssembler.toCommandFromResource(storeId, body);
        var result = commandService.handle(command).orElseThrow();
        return ResponseEntity.ok(PriceErrorResponseFromEntityAssembler.toResourceFromEntity(result));
    }

    @PostMapping("/journeys/{recorridoId}/savings")
    @Operation(summary = "Request savings calculation (Consumer)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Savings calculated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<SavingsResponse> calculateSavings(
            @PathVariable String recorridoId,
            @Valid @RequestBody CalculateSavingsResource body) {
        var command = CalculateSavingsCommandFromResourceAssembler.toCommandFromResource(recorridoId, body);
        var result = commandService.handle(command).orElseThrow();
        return ResponseEntity.ok(SavingsResponseFromEntityAssembler.toResourceFromEntity(result));
    }

    @GetMapping("/journeys/{recorridoId}/savings")
    @Operation(summary = "Get calculated savings for a journey (Consumer)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Savings retrieved"),
            @ApiResponse(responseCode = "404", description = "Savings not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<SavingsResponse> getSavings(@PathVariable String recorridoId) {
        var query = new GetSavingsByJourneyQuery(recorridoId);
        var result = queryService.handle(query);
        return result.map(s -> ResponseEntity.ok(SavingsResponseFromEntityAssembler.toResourceFromEntity(s)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ================== MERCHANT ENDPOINTS ==================

    @GetMapping("/stores/{storeId}/trust-profile")
    @Operation(summary = "Get store trust profile (Merchant)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trust profile retrieved"),
            @ApiResponse(responseCode = "404", description = "Store not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TrustProfileResponse> getTrustProfile(@PathVariable String storeId) {
        var query = new GetTrustProfileQuery(storeId);
        var result = queryService.handle(query);
        return result.map(t -> ResponseEntity.ok(TrustProfileResponseFromEntityAssembler.toResourceFromEntity(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/stores/{storeId}/ratings")
    @Operation(summary = "Get all ratings for a store (Merchant)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ratings retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<RatingResponse>> getAllRatings(@PathVariable String storeId) {
        var query = new GetAllRatingsQuery(storeId);
        var result = queryService.handle(query);
        return ResponseEntity.ok(result.stream()
                .map(RatingResponseFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/stores/{storeId}/reviews/all")
    @Operation(summary = "Get all reviews including pending (Merchant)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reviews retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ReviewResponse>> getAllReviews(
            @PathVariable String storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var query = new GetAllReviewsQuery(storeId, page, size);
        var result = queryService.handle(query);
        return ResponseEntity.ok(result.stream()
                .map(ReviewResponseFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @PostMapping("/stores/{storeId}/reviews/{reseñaId}/reply")
    @Operation(summary = "Reply to a review (Merchant)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reply posted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ReviewResponse> replyToReview(
            @PathVariable String storeId,
            @PathVariable String reseñaId,
            @Valid @RequestBody ReplyReviewResource body) {
        var command = ReplyReviewCommandFromResourceAssembler.toCommandFromResource(storeId, reseñaId, body);
        var result = commandService.handle(command).orElseThrow();
        return ResponseEntity.ok(ReviewResponseFromEntityAssembler.toResourceFromEntity(result));
    }

    @GetMapping("/stores/{storeId}/price-errors")
    @Operation(summary = "Get price errors reported for a store (Merchant)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Price errors retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<PriceErrorResponse>> getPriceErrors(@PathVariable String storeId) {
        var query = new GetPriceErrorsByStoreQuery(storeId);
        var result = queryService.handle(query);
        return ResponseEntity.ok(result.stream()
                .map(PriceErrorResponseFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @PatchMapping("/stores/{storeId}/price-errors/{errorId}/confirm")
    @Operation(summary = "Confirm or reject a price error (Merchant)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Price error updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Price error not found")
    })
    public ResponseEntity<PriceErrorResponse> confirmPriceError(
            @PathVariable String storeId,
            @PathVariable String errorId,
            @Valid @RequestBody ConfirmPriceErrorResource body) {
        var command = ConfirmPriceErrorCommandFromResourceAssembler.toCommandFromResource(storeId, errorId, body);
        var result = commandService.handle(command).orElseThrow();
        return ResponseEntity.ok(PriceErrorResponseFromEntityAssembler.toResourceFromEntity(result));
    }
}