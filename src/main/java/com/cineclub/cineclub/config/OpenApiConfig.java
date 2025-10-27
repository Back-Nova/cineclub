package com.cineclub.cineclub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Cineclub API")
                .version("1.0")
                .description("API REST para gestión de cartelera de cine - Catálogo, Salas y Funciones")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Servidor de desarrollo")
            ));
    }
}

