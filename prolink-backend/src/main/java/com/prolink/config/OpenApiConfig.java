package com.prolink.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    /**
     * Configuración principal de OpenAPI
     */
    @Bean
    public OpenAPI proLinkOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(Arrays.asList(
                    new Server().url("http://localhost:8080" + contextPath).description("Servidor de Desarrollo"),
                    new Server().url("https://api.prolink.com" + contextPath).description("Servidor de Producción")
                ))
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", 
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("Autenticación JWT Bearer Token")
                    )
                )
                .addSecurityItem(new SecurityRequirement()
                    .addList("bearerAuth"));
    }

    /**
     * Información básica de la API
     */
    private Info apiInfo() {
        return new Info()
                .title("ProLink API")
                .description("API REST para ProLink - Plataforma profesional tipo LinkedIn")
                .version("1.0.0")
                .termsOfService("https://prolink.com/terms")
                .contact(new Contact()
                    .name("Equipo ProLink")
                    .email("contact@prolink.com")
                    .url("https://prolink.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT"));
    }
}