package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal de Spring Boot para el detector de mutantes.
 * 
 * Esta aplicación implementa una API REST que permite:
 * - Detectar si una secuencia de ADN pertenece a un mutante
 * - Obtener estadísticas de las verificaciones realizadas
 * 
 * @author MercadoLibre Backend Exam
 * @version 1.0.0
 */
@SpringBootApplication
public class MutantDetectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutantDetectorApplication.class, args);
    }
}
