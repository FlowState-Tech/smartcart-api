package com.smartcart.storemanagement.interfaces.rest;

import com.smartcart.storemanagement.domain.model.queries.GetMerchantProfileQuery;
import com.smartcart.storemanagement.domain.services.MerchantProfileQueryService;
import com.smartcart.storemanagement.interfaces.rest.resources.MerchantProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/merchants", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Merchant Profile", description = "Perfil del comerciante autenticado")
@SecurityRequirement(name = "bearerAuth")
public class MerchantProfileController {

    private final MerchantProfileQueryService merchantProfileQueryService;

    public MerchantProfileController(MerchantProfileQueryService merchantProfileQueryService) {
        this.merchantProfileQueryService = merchantProfileQueryService;
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener perfil del comerciante autenticado",
               description = "Devuelve el estado de verificación y las tiendas asociadas al comerciante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil del comerciante obtenido correctamente."),
            @ApiResponse(responseCode = "401", description = "Token ausente, expirado o inválido."),
            @ApiResponse(responseCode = "404", description = "Comerciante no encontrado.")
    })
    public ResponseEntity<MerchantProfileResponse> getMerchantProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        var query = new GetMerchantProfileQuery(userDetails.getUsername());
        return merchantProfileQueryService.handle(query)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
