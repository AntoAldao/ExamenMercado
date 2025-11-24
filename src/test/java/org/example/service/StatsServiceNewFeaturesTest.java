package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para las nuevas funcionalidades de StatsService.
 * Tests para el método getStatsByDateRange.
 */
@ExtendWith(MockitoExtension.class)
class StatsServiceNewFeaturesTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("getStatsByDateRange debe retornar stats con fechas válidas")
    void testGetStatsByDateRange_WithValidDates() {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 7);
        
        when(dnaRecordRepository.countMutantsByDateRange(startDate, endDate)).thenReturn(40L);
        when(dnaRecordRepository.countHumansByDateRange(startDate, endDate)).thenReturn(100L);

        // Act
        StatsResponse response = statsService.getStatsByDateRange(startDate, endDate);

        // Assert
        assertNotNull(response);
        assertEquals(40L, response.getCountMutantDna());
        assertEquals(100L, response.getCountHumanDna());
        assertEquals(0.4, response.getRatio(), 0.001);
        
        verify(dnaRecordRepository).countMutantsByDateRange(startDate, endDate);
        verify(dnaRecordRepository).countHumansByDateRange(startDate, endDate);
    }

    @Test
    @DisplayName("getStatsByDateRange debe manejar solo startDate")
    void testGetStatsByDateRange_WithOnlyStartDate() {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        
        when(dnaRecordRepository.countMutantsByDateRange(startDate, null)).thenReturn(25L);
        when(dnaRecordRepository.countHumansByDateRange(startDate, null)).thenReturn(75L);

        // Act
        StatsResponse response = statsService.getStatsByDateRange(startDate, null);

        // Assert
        assertNotNull(response);
        assertEquals(25L, response.getCountMutantDna());
        assertEquals(75L, response.getCountHumanDna());
        assertEquals(0.333, response.getRatio(), 0.01);
    }

    @Test
    @DisplayName("getStatsByDateRange debe manejar solo endDate")
    void testGetStatsByDateRange_WithOnlyEndDate() {
        // Arrange
        LocalDate endDate = LocalDate.of(2025, 1, 7);
        
        when(dnaRecordRepository.countMutantsByDateRange(null, endDate)).thenReturn(30L);
        when(dnaRecordRepository.countHumansByDateRange(null, endDate)).thenReturn(60L);

        // Act
        StatsResponse response = statsService.getStatsByDateRange(null, endDate);

        // Assert
        assertNotNull(response);
        assertEquals(30L, response.getCountMutantDna());
        assertEquals(60L, response.getCountHumanDna());
        assertEquals(0.5, response.getRatio(), 0.001);
    }

    @Test
    @DisplayName("getStatsByDateRange debe manejar ratio cuando no hay humanos")
    void testGetStatsByDateRange_WithNoHumans() {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 7);
        
        when(dnaRecordRepository.countMutantsByDateRange(startDate, endDate)).thenReturn(50L);
        when(dnaRecordRepository.countHumansByDateRange(startDate, endDate)).thenReturn(0L);

        // Act
        StatsResponse response = statsService.getStatsByDateRange(startDate, endDate);

        // Assert
        assertNotNull(response);
        assertEquals(50L, response.getCountMutantDna());
        assertEquals(0L, response.getCountHumanDna());
        assertEquals(0.0, response.getRatio(), 0.001);
    }

    @Test
    @DisplayName("getStatsByDateRange debe manejar contadores en cero")
    void testGetStatsByDateRange_WithZeroCounts() {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 7);
        
        when(dnaRecordRepository.countMutantsByDateRange(startDate, endDate)).thenReturn(0L);
        when(dnaRecordRepository.countHumansByDateRange(startDate, endDate)).thenReturn(0L);

        // Act
        StatsResponse response = statsService.getStatsByDateRange(startDate, endDate);

        // Assert
        assertNotNull(response);
        assertEquals(0L, response.getCountMutantDna());
        assertEquals(0L, response.getCountHumanDna());
        assertEquals(0.0, response.getRatio(), 0.001);
    }

    @Test
    @DisplayName("getStatsByDateRange debe manejar fechas iguales")
    void testGetStatsByDateRange_WithSameDates() {
        // Arrange
        LocalDate date = LocalDate.of(2025, 1, 5);
        
        when(dnaRecordRepository.countMutantsByDateRange(date, date)).thenReturn(5L);
        when(dnaRecordRepository.countHumansByDateRange(date, date)).thenReturn(10L);

        // Act
        StatsResponse response = statsService.getStatsByDateRange(date, date);

        // Assert
        assertNotNull(response);
        assertEquals(5L, response.getCountMutantDna());
        assertEquals(10L, response.getCountHumanDna());
        assertEquals(0.5, response.getRatio(), 0.001);
    }
}
