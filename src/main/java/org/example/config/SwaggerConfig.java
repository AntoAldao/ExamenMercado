package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para documentación de la API.
 * 
 * Acceder a: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Mutant Detector API")
                .version("1.0.0")
                .description("API REST para detectar mutantes mediante análisis de secuencias de ADN. " +
                            "Proyecto de examen técnico para MercadoLibre Backend Developer.")
                .contact(new Contact()
                    .name("MercadoLibre Backend Exam")
                    .email("exam@mercadolibre.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
