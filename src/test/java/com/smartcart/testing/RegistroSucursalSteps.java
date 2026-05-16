package com.upc.smartcart.testing.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StoreIntegrationTesting {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenComplexStoreJson_whenPostToStoresEndpoint_thenReturn201Created() throws Exception {
        // Estructura idéntica al payload real de la documentación
        String jsonRequestBody = "{\n" +
                "  \"merchantId\": \"M-999\",\n" +
                "  \"name\": \"Metro - Monterrico\",\n" +
                "  \"ruc\": \"20100435671\",\n" +
                "  \"address\": {\n" +
                "    \"street\": \"Av. Prolongación San Juan 456\",\n" +
                "    \"district\": \"San Juan de Miraflores\",\n" +
                "    \"latitude\": -12.156,\n" +
                "    \"longitude\": -76.983\n" +
                "  },\n" +
                "  \"operatingHours\": [\n" +
                "    {\n" +
                "      \"day\": \"Monday\",\n" +
                "      \"open\": \"08:00\",\n" +
                "      \"close\": \"22:00\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mockMvc.perform(post("/api/v1/store-management/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.storeId").value(101))
                .andExpect(jsonPath("$.status").value("PENDING_VERIFICATION"));
    }
}