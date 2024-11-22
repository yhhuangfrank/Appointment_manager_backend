package com.frank.user.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userAPI() {
        return GroupedOpenApi.builder()
                .group("user-module")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI docsOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("user-management-project")
                                .description("REST APIs")
                                .version("v1.0")
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("docs-test")
                );
    }
}

