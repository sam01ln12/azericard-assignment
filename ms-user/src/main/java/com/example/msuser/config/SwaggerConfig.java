package com.example.msuser.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    private static final String SECURITY_KEY = "JWT";
    private static final String SECURITY_SCHEME = "bearer";
    private static final String SECURITY_FORMAT = "jwt";
    private static final String SECURITY_NAME = "Authorization";
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("ms-user")
                .packagesToScan("com.example.msuser")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("User API")
                        .description("MS-USER API")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_KEY,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .scheme(SECURITY_SCHEME)
                                        .bearerFormat(SECURITY_FORMAT)
                                        .in(SecurityScheme.In.HEADER)
                                        .name(SECURITY_NAME)))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_KEY));
    }
}
