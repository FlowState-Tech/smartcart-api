package com.smartcart.verification.interfaces.rest;

import com.smartcart.verification.application.internal.commandservices.VerificationCommandServiceImpl;
import com.smartcart.verification.domain.model.commands.RegisterVerificationApplicationCommand;
import com.smartcart.verification.interfaces.rest.resources.RegisterVerificationRequest;
import com.smartcart.verification.interfaces.rest.resources.VerificationApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/verification", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Verification", description = "Verification Endpoints")
public class VerificationController {

    private final VerificationCommandServiceImpl verificationCommandService;

    public VerificationController(VerificationCommandServiceImpl verificationCommandService) {
        this.verificationCommandService = verificationCommandService;
    }

    @PostMapping("/applications")
    @Operation(summary = "Register verification application", description = "Submits a company RUC for verification before store branch setup.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application processed and saved successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.")
    })
    public ResponseEntity<VerificationApplicationResponse> registerApplication(@RequestBody RegisterVerificationRequest request) {
        var command = new RegisterVerificationApplicationCommand(request.merchantId(), request.ruc());
        var result = verificationCommandService.handle(command);

        if (result.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var app = result.get();
        var response = new VerificationApplicationResponse(
                app.getId(),
                app.getMerchantId(),
                app.getRuc().getNormalized(),
                app.getCompanyName(),
                app.getStatus().name()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}