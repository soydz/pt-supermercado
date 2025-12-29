package com.soydz.ptsupermercado.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@OpenAPIDefinition(
    info =
        @Info(
            title = "Prueba Técnica Spring Boot - Supermercado",
            version = "0.0.1",
            description = "Gestión de una cadena de supermercados"))
@Configuration
public class OpenApiConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("public-api").pathsToMatch("/api/**").build();
  }
}
