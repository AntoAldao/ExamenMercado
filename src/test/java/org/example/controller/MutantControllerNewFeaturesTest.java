package org.example.controller;

import org.example.dto.HealthResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests de integración para las nuevas funcionalidades del MutantController.
 * Tests para los ejercicios implementados: Health Check, Delete, Stats con filtros.
 */
@WebMvcTest(MutantController.class)
class MutantControllerNewFeaturesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("GET /health debe retornar 200 y estructura correcta")
    void testHealthCheck_ShouldReturnOkWithCorrectStructure() throws Exception {
        mockMvc.perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.service").value("Mutant Detector API"));
    }

    @Test
    @DisplayName("GET / debe retornar 200 y mensaje simple")
    void testRootHealthCheck_ShouldReturnOkWithMessage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string("Mutant Detector API is running!"));
    }

    @Test
    @DisplayName("DELETE /mutant/{hash} debe retornar 204 cuando se elimina exitosamente")
    void testDeleteDna_ShouldReturn204_WhenDeleted() throws Exception {
        String hash = "3a5f2c9e8b1d4f7a6c3e9d2b8f5a1c7e4d9b2f6a8c3e5d1b7f4a9c2e6d8b3f5a1";
        
        mockMvc.perform(delete("/mutant/{hash}", hash))
            .andExpect(status().isNoContent());
    }
}
