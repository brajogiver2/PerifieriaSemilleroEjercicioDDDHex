package com.co.periferia.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CONFIGURACIÓN: OpenAPI / Swagger 3.0                       ║
 * ║  Define la documentación interactiva de la API REST          ║
 * ║  Accesible en: http://localhost:8080/swagger-ui.html        ║
 * ║  JSON del esquema: http://localhost:8080/v3/api-docs        ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Períferia API")
                        .description("API REST para la gestión de usuarios en el sistema Períferia\n\n" +
                                "Esta API implementa arquitectura hexagonal con puertos y adaptadores.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("desarrollo@periferia.com")
                                .url("https://github.com/tuempresa/periferia"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor local"),
                        new Server()
                                .url("https://api-staging.periferia.com")
                                .description("Ambiente de staging"),
                        new Server()
                                .url("https://api.periferia.com")
                                .description("Ambiente de producción")
                ));
    }
}
