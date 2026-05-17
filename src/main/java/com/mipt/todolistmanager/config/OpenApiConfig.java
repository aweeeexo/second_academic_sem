package com.mipt.todolistmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("To-Do List API")
            .version("2.0.0")
            .description("To-Do List Manager with file attachments, favorites, and preferences")
            .contact(new Contact()
                .name("Developer")
                .email("dev@example.com")
                .url("https://example.com")));
  }
}