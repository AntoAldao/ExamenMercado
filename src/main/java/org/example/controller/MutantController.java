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
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * 
     * @return StatsResponse con contadores y ratio
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Obtener estadísticas",
        description = "Retorna la cantidad de ADN mutantes, humanos y el ratio entre ellos."
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
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
