package com.embarkx.sbecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                .info(new Info()
                        .title("Spring Boot E-commerce API")
                        .version("1.0.0")
                        .description("API documentation for Spring Boot E-commerce API")
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact()
                                .name("Spring Boot Boumirooster")
                                .email("springbootboumirooster@gmail.com")
                                .url("http://www.springboot-commerce.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("http://www.springboot-commerce.com"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTHENTICATION, bearerScheme))
                .addSecurityItem(bearerRequirement);
    }
}
