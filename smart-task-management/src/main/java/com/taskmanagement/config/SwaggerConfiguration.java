package com.taskmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI Configuration
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Task Management System API")
                        .version("1.0.0")
                        .description("AI-Powered Smart Task Management Backend API")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@taskmanagement.com")
                                .url("https://taskmanagement.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearer_token"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearer_token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")));
    }
}
