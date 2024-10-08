package com.frank.hosp.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi adminAPI() {
        return GroupedOpenApi.builder()
                .group("Admin-module")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public OpenAPI docsOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("hospital-management-project")
                                .description("REST APIs")
                                .version("v1.0")
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("docs-test")
                                .url("https://google.com")
                );
    }
}
