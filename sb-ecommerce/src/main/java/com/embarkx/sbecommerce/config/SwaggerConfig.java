package com.embarkx.sbecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String BEARER_AUTHENTICATION = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI(){
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Bearer Token");

        SecurityRequirement bearerRequirement = new SecurityRequirement();
        bearerRequirement.addList(BEARER_AUTHENTICATION);

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTHENTICATION, bearerScheme))
                .addSecurityItem(bearerRequirement);
    }
}
