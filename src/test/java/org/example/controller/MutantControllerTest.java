package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para MutantController.
 * 
 * Objetivo:
 * - Probar endpoints REST completos
 * - Validar códigos de respuesta HTTP
 * - Verificar serialización JSON
 * - Probar validaciones de entrada
 * 
 * Patrón: @WebMvcTest + MockMvc
 */
@WebMvcTest(MutantController.class)
@DisplayName("MutantController - Tests de Integración REST")
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    // ==========================================
    // TESTS POST /mutant
    // ==========================================

    @Test
    @DisplayName("POST /mutant - Debe retornar 200 OK cuando es mutante")
    void testIsMutantReturnOk() throws Exception {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        DnaRequest request = new DnaRequest(dna);
        
        when(mutantService.analyzeDna(any())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant - Debe retornar 403 FORBIDDEN cuando es humano")
    void testIsMutantReturnForbidden() throws Exception {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATTT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        DnaRequest request = new DnaRequest(dna);
        
        when(mutantService.analyzeDna(any())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant - Debe retornar 400 BAD REQUEST cuando el DNA es null")
    void testIsMutantWithNullDna() throws Exception {
        // Arrange
        DnaRequest request = new DnaRequest(null);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant - Debe retornar 400 BAD REQUEST cuando el DNA está vacío")
    void testIsMutantWithEmptyDna() throws Exception {
        // Arrange
        String[] dna = {};
        DnaRequest request = new DnaRequest(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant - Debe retornar 400 BAD REQUEST cuando la matriz no es NxN")
    void testIsMutantWithNonSquareMatrix() throws Exception {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTAT"    // Fila más corta
        };
        DnaRequest request = new DnaRequest(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant - Debe retornar 400 BAD REQUEST cuando hay caracteres inválidos")
    void testIsMutantWithInvalidCharacters() throws Exception {
        // Arrange
        String[] dna = {
            "ATGC",
            "CXGT",  // X es inválido
            "TGAT",
            "GCAT"
        };
        DnaRequest request = new DnaRequest(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ==========================================
    // TESTS GET /stats
    // ==========================================

    @Test
    @DisplayName("GET /stats - Debe retornar estadísticas correctamente")
    void testGetStatsSuccess() throws Exception {
        // Arrange
        StatsResponse statsResponse = new StatsResponse(40L, 100L, 0.4);
        when(statsService.getStats()).thenReturn(statsResponse);

        // Act & Assert
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats - Debe retornar estadísticas en cero cuando no hay datos")
    void testGetStatsWithNoData() throws Exception {
        // Arrange
        StatsResponse statsResponse = new StatsResponse(0L, 0L, 0.0);
        when(statsService.getStats()).thenReturn(statsResponse);

        // Act & Assert
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count_mutant_dna").value(0))
                .andExpect(jsonPath("$.count_human_dna").value(0))
                .andExpect(jsonPath("$.ratio").value(0.0));
    }
}
