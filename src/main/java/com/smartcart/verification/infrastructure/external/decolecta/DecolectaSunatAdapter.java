package com.smartcart.verification.infrastructure.external.decolecta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcart.verification.domain.model.valueobjects.Ruc;
import com.smartcart.verification.domain.services.SunatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class DecolectaSunatAdapter implements SunatService {

    private final RestClient restClient;
    private final String apiToken;

    public DecolectaSunatAdapter(@Value("${decolecta.api.base-url:https://api.decolecta.com/v1}") String baseUrl,
                                 @Value("${decolecta.api.token}") String apiToken) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiToken = apiToken;
    }

    @Override
    public Optional<String> fetchCompanyNameIfActive(Ruc ruc) {
        try {
            DecolectaRucResponse response = restClient.get()
                    .uri("/sunat/ruc?numero={numero}&token={token}", ruc.getNormalized(), apiToken)
                    .retrieve()
                    .body(DecolectaRucResponse.class);

            // Validamos usando las nuevas propiedades mapeadas
            if (response != null && "ACTIVO".equalsIgnoreCase(response.estado()) && "HABIDO".equalsIgnoreCase(response.condicion())) {
                return Optional.of(response.razonSocial());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error calling Decolecta: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record DecolectaRucResponse(
        @JsonProperty("razon_social") String razonSocial,
        @JsonProperty("numero_documento") String numeroDocumento,
        @JsonProperty("estado") String estado,
        @JsonProperty("condicion") String condicion
) {}