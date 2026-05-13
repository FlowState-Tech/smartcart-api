package com.smartcart.notification.interfaces.rest;

import com.smartcart.notification.domain.model.aggregates.UserPreference;
import com.smartcart.notification.domain.model.commands.SendTestNotificationCommand;
import com.smartcart.notification.domain.model.queries.GetNotificationHistoryQuery;
import com.smartcart.notification.domain.model.queries.GetUserPreferencesQuery;
import com.smartcart.notification.domain.services.NotificationCommandService;
import com.smartcart.notification.domain.services.NotificationQueryService;
import com.smartcart.notification.domain.services.UserPreferenceCommandService;
import com.smartcart.notification.domain.services.UserPreferenceQueryService;
import com.smartcart.notification.interfaces.rest.resources.NotificationHistoryPageResource;
import com.smartcart.notification.interfaces.rest.resources.NotificationStatusResponse;
import com.smartcart.notification.interfaces.rest.resources.PreferenceResponse;
import com.smartcart.notification.interfaces.rest.resources.TestNotificationResource;
import com.smartcart.notification.interfaces.rest.resources.UpdatePreferencesResource;
import com.smartcart.notification.interfaces.rest.transform.NotificationStatusResponseFromEntityAssembler;
import com.smartcart.notification.interfaces.rest.transform.NotificationSummaryResourceFromEntityAssembler;
import com.smartcart.notification.interfaces.rest.transform.PreferenceResponseFromEntityAssembler;
import com.smartcart.notification.interfaces.rest.transform.UpdatePreferencesCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for the Notification bounded context (preferences, history, test send).
 */
@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Notification and user preference endpoints")
public class NotificationsController {

    private final UserPreferenceCommandService userPreferenceCommandService;
    private final UserPreferenceQueryService userPreferenceQueryService;
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    public NotificationsController(UserPreferenceCommandService userPreferenceCommandService,
                                   UserPreferenceQueryService userPreferenceQueryService,
                                   NotificationCommandService notificationCommandService,
                                   NotificationQueryService notificationQueryService) {
        this.userPreferenceCommandService = userPreferenceCommandService;
        this.userPreferenceQueryService = userPreferenceQueryService;
        this.notificationCommandService = notificationCommandService;
        this.notificationQueryService = notificationQueryService;
    }

    @PostMapping("/preferences")
    @Operation(summary = "Update communication preferences")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preferences saved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PreferenceResponse> updatePreferences(@Valid @RequestBody UpdatePreferencesResource body) {
        var command = UpdatePreferencesCommandFromResourceAssembler.toCommandFromResource(body);
        var saved = userPreferenceCommandService.handle(command).orElseThrow();
        return ResponseEntity.ok(PreferenceResponseFromEntityAssembler.toResourceFromEntity(saved));
    }

    @GetMapping("/preferences/{userId}")
    @Operation(summary = "Get preferences by user id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PreferenceResponse> getPreferences(@PathVariable Long userId) {
        Optional<UserPreference> pref =
                userPreferenceQueryService.handle(new GetUserPreferencesQuery(userId));
        return pref.map(p -> ResponseEntity.ok(PreferenceResponseFromEntityAssembler.toResourceFromEntity(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/history/{userId}")
    @Operation(summary = "Paginated notification history for a user")
    public ResponseEntity<NotificationHistoryPageResource> getHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var result = notificationQueryService.handle(new GetNotificationHistoryQuery(userId, page, size));
        var content = result.getContent().stream().map(NotificationSummaryResourceFromEntityAssembler::toResourceFromEntity).toList();
        var pageRes = new NotificationHistoryPageResource(
                content,
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
        return ResponseEntity.ok(pageRes);
    }

    @PostMapping("/send-test")
    @Operation(summary = "Send a test notification (validates channel / stub gateway)")
    public ResponseEntity<NotificationStatusResponse> sendTest(@Valid @RequestBody TestNotificationResource body) {
        var title = Optional.ofNullable(body.titulo()).orElse("");
        var command = new SendTestNotificationCommand(body.userId(), body.channel(), title, body.cuerpo());
        var sent = notificationCommandService.handle(command).orElseThrow();
        return ResponseEntity.ok(NotificationStatusResponseFromEntityAssembler.toResourceFromEntity(sent));
    }
}
