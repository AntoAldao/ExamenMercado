package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.DnaRequest;
import org.example.dto.HealthResponse;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Controlador REST para endpoints de detección de mutantes.
 * 
 * Endpoints:
 * - POST /mutant/ - Detecta si un ADN es mutante
 * - GET /stats - Obtiene estadísticas de verificaciones
 * 
 * Patrón: REST Controller + Dependency Injection
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Mutant Detection", description = "API para detección de mutantes y estadísticas")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    /**
     * GET / - Health check simple
     */
    @GetMapping("/")
    public ResponseEntity<String> rootHealthCheck() {
        return ResponseEntity.ok("Mutant Detector API is running!");
    }

    /**
     * GET /health - Health check detallado
     * 
     * Retorna el estado de la aplicación con información adicional.
     * 
     * @return ResponseEntity con HealthResponse (status, timestamp, service)
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Verifica el estado de la aplicación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aplicación funcionando correctamente",
                     content = @Content(schema = @Schema(implementation = HealthResponse.class)))
    })
    public ResponseEntity<HealthResponse> healthCheck() {
        HealthResponse response = new HealthResponse(
            "UP",
            LocalDateTime.now(),
            "Mutant Detector API"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * POST /mutant/
     * 
     * Detecta si una secuencia de ADN pertenece a un mutante.
     * 
     * @param request DTO con el array de ADN
     * @return 200 OK si es mutante, 403 FORBIDDEN si es humano
     */
    @PostMapping("/mutant")
    @Operation(
        summary = "Detectar mutante",
        description = "Analiza una secuencia de ADN y determina si pertenece a un mutante. " +
                      "Retorna 200-OK si es mutante, 403-Forbidden si es humano."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Es mutante",
            content = @Content(schema = @Schema(implementation = Void.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Es humano (no mutante)",
            content = @Content(schema = @Schema(implementation = Void.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ADN inválido (matriz no NxN, caracteres inválidos, etc.)",
            content = @Content
        )
    })
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.analyzeDna(request.getDna());
        
        if (isMutant) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * GET /stats
     * 
     * Obtiene estadísticas de las verificaciones de ADN realizadas.
     * Opcionalmente puede filtrar por rango de fechas.
     * 
     * @param startDate Fecha de inicio (opcional, formato: YYYY-MM-DD)
     * @param endDate Fecha de fin (opcional, formato: YYYY-MM-DD)
     * @return StatsResponse con contadores y ratio
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Obtener estadísticas",
        description = "Retorna la cantidad de ADN mutantes, humanos y el ratio entre ellos. " +
                      "Opcionalmente puede filtrar por rango de fechas usando startDate y endDate."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estadísticas obtenidas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StatsResponse.class)
            )
        )
    })
    public ResponseEntity<StatsResponse> getStats(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        StatsResponse stats;
        if (startDate != null || endDate != null) {
            stats = statsService.getStatsByDateRange(startDate, endDate);
        } else {
            stats = statsService.getStats();
        }
        return ResponseEntity.ok(stats);
    }

    /**
     * DELETE /mutant/{hash}
     * 
     * Elimina un registro de ADN por su hash SHA-256.
     * 
     * @param hash Hash SHA-256 del ADN a eliminar
     * @return ResponseEntity con 204 No Content si se eliminó, 404 si no se encontró
     */
    @DeleteMapping("/mutant/{hash}")
    @Operation(
        summary = "Eliminar registro de ADN",
        description = "Elimina un análisis de ADN previo usando su hash SHA-256."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Registro eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Registro no encontrado"
        )
    })
    public ResponseEntity<Void> deleteDna(@PathVariable String hash) {
        mutantService.deleteDnaRecord(hash);
        return ResponseEntity.noContent().build();
    }
}
