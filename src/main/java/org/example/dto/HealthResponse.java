package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para el endpoint de salud.
 * Retorna el estado de la aplicación y timestamp.
 */
@Data
@AllArgsConstructor
public class HealthResponse {
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @JsonProperty("service")
    private String service;
}
