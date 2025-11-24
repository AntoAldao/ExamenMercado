package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios con Mocks para StatsService.
 * 
 * Objetivo:
 * - Verificar cálculo correcto de estadísticas
 * - Probar casos edge (división por cero, sin datos)
 * - Validar interacciones con Repository
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StatsService - Tests Unitarios con Mocks")
class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("Debe calcular estadísticas correctamente con datos normales")
    void testGetStatsWithNormalData() {
        // Arrange: 40 mutantes, 100 humanos
        when(dnaRecordRepository.countMutants()).thenReturn(40L);
        when(dnaRecordRepository.countHumans()).thenReturn(100L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertNotNull(stats);
        assertEquals(40L, stats.getCountMutantDna());
        assertEquals(100L, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio(), 0.001);  // 40/100 = 0.4
        
        verify(dnaRecordRepository, times(1)).countMutants();
        verify(dnaRecordRepository, times(1)).countHumans();
    }

    @Test
    @DisplayName("Debe calcular ratio 1.0 cuando hay igual cantidad de mutantes y humanos")
    void testGetStatsWithEqualCounts() {
        // Arrange: 50 mutantes, 50 humanos
        when(dnaRecordRepository.countMutants()).thenReturn(50L);
        when(dnaRecordRepository.countHumans()).thenReturn(50L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(50L, stats.getCountMutantDna());
        assertEquals(50L, stats.getCountHumanDna());
        assertEquals(1.0, stats.getRatio(), 0.001);  // 50/50 = 1.0
    }

    @Test
    @DisplayName("Debe manejar correctamente cuando no hay humanos (división por cero)")
    void testGetStatsWithZeroHumans() {
        // Arrange: 10 mutantes, 0 humanos
        when(dnaRecordRepository.countMutants()).thenReturn(10L);
        when(dnaRecordRepository.countHumans()).thenReturn(0L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert: Ratio debe ser 0 para evitar división por cero
        assertEquals(10L, stats.getCountMutantDna());
        assertEquals(0L, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe retornar estadísticas en cero cuando no hay datos")
    void testGetStatsWithNoData() {
        // Arrange: 0 mutantes, 0 humanos
        when(dnaRecordRepository.countMutants()).thenReturn(0L);
        when(dnaRecordRepository.countHumans()).thenReturn(0L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(0L, stats.getCountMutantDna());
        assertEquals(0L, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe calcular ratio cuando hay más mutantes que humanos")
    void testGetStatsWithMoreMutantsThanHumans() {
        // Arrange: 150 mutantes, 50 humanos
        when(dnaRecordRepository.countMutants()).thenReturn(150L);
        when(dnaRecordRepository.countHumans()).thenReturn(50L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(150L, stats.getCountMutantDna());
        assertEquals(50L, stats.getCountHumanDna());
        assertEquals(3.0, stats.getRatio(), 0.001);  // 150/50 = 3.0
    }

    @Test
    @DisplayName("Debe calcular ratio decimal correctamente")
    void testGetStatsWithDecimalRatio() {
        // Arrange: 33 mutantes, 100 humanos
        when(dnaRecordRepository.countMutants()).thenReturn(33L);
        when(dnaRecordRepository.countHumans()).thenReturn(100L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(33L, stats.getCountMutantDna());
        assertEquals(100L, stats.getCountHumanDna());
        assertEquals(0.33, stats.getRatio(), 0.001);  // 33/100 = 0.33
    }
}
