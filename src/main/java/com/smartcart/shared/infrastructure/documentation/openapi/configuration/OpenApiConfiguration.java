package com.smartcart.shared.infrastructure.documentation.openapi.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

@Configuration
public class OpenApiConfiguration {
    // Properties
    @Value("${spring.application.name}")
    String applicationName;

    @Value("${documentation.application.description}")
    String applicationDescription;

    @Value("${documentation.application.version}")
    String applicationVersion;

    // Methods

    @Bean
    public OpenAPI smartcartOpenApi() {
        // General configuration
        var openApi = new OpenAPI();
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de Desarrollo (Local)");

        Server prodServer = new Server();
        prodServer.setUrl("https://smartcart-api-production.up.railway.app"); // <--- Tu URL de Railway
        prodServer.setDescription("Servidor de Producción (Railway)");
        openApi
                .info(new Info()
                        .title(this.applicationName)
                        .description(this.applicationDescription)
                        .version(this.applicationVersion)
                        .license(new License().name("Apache 2.0")
                                .url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SmartCart wiki Documentation")
                        .url("https://smartcart.wiki.github.io/docs"))
                .servers(List.of(devServer, prodServer));
        // Add a security scheme

       final String securitySchemeName = "bearerAuth";

       openApi.addSecurityItem(new SecurityRequirement()
                       .addList(securitySchemeName))
               .components(new Components()
                       .addSecuritySchemes(securitySchemeName,
                               new SecurityScheme()
                                       .name(securitySchemeName)
                                       .type(SecurityScheme.Type.HTTP)
                                       .scheme("bearer")
                                       .bearerFormat("JWT")));

        // Return the OpenAPI configuration object with all the settings

        return openApi;
    }
}
