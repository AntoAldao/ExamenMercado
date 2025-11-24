package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios con Mocks para MutantService.
 * 
 * Objetivo:
 * - Probar la lógica de negocio aislada
 * - Verificar interacciones con Repository y MutantDetector
 * - Probar el sistema de caché
 * 
 * Patrón: Mockito (@Mock, @InjectMocks)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MutantService - Tests Unitarios con Mocks")
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    private String[] mutantDna;
    private String[] humanDna;

    @BeforeEach
    void setUp() {
        mutantDna = new String[]{
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        humanDna = new String[]{
            "ATGCGA",
            "CAGTGC",
            "TTATTT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
    }

    @Test
    @DisplayName("Debe analizar y guardar ADN mutante nuevo")
    void testAnalyzeMutantDnaNotInCache() {
        // Arrange: Simular que no existe en BD
        when(dnaRecordRepository.findByDnaSequence(anyString()))
            .thenReturn(Optional.empty());
        
        // Simular que el algoritmo detecta mutante
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);
        
        // Simular guardado exitoso
        when(dnaRecordRepository.save(any(DnaRecord.class)))
            .thenReturn(new DnaRecord(mutantDna, true));

        // Act: Llamar al servicio
        boolean result = mutantService.analyzeDna(mutantDna);

        // Assert: Verificar resultado y llamadas
        assertTrue(result);
        verify(dnaRecordRepository, times(1)).findByDnaSequence(anyString());
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe analizar y guardar ADN humano nuevo")
    void testAnalyzeHumanDnaNotInCache() {
        // Arrange
        when(dnaRecordRepository.findByDnaSequence(anyString()))
            .thenReturn(Optional.empty());
        
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);
        
        when(dnaRecordRepository.save(any(DnaRecord.class)))
            .thenReturn(new DnaRecord(humanDna, false));

        // Act
        boolean result = mutantService.analyzeDna(humanDna);

        // Assert
        assertFalse(result);
        verify(dnaRecordRepository, times(1)).findByDnaSequence(anyString());
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe usar caché si el ADN mutante ya existe en BD")
    void testAnalyzeMutantDnaFromCache() {
        // Arrange: Simular que YA existe en BD
        DnaRecord cachedRecord = new DnaRecord(1L, "ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG", true);
        when(dnaRecordRepository.findByDnaSequence(anyString()))
            .thenReturn(Optional.of(cachedRecord));

        // Act
        boolean result = mutantService.analyzeDna(mutantDna);

        // Assert: NO debe llamar al detector ni guardar
        assertTrue(result);
        verify(dnaRecordRepository, times(1)).findByDnaSequence(anyString());
        verify(mutantDetector, never()).isMutant(any());  // NO debe analizar
        verify(dnaRecordRepository, never()).save(any());  // NO debe guardar
    }

    @Test
    @DisplayName("Debe usar caché si el ADN humano ya existe en BD")
    void testAnalyzeHumanDnaFromCache() {
        // Arrange
        DnaRecord cachedRecord = new DnaRecord(2L, "ATGCGA,CAGTGC,TTATTT,AGACGG,GCGTCA,TCACTG", false);
        when(dnaRecordRepository.findByDnaSequence(anyString()))
            .thenReturn(Optional.of(cachedRecord));

        // Act
        boolean result = mutantService.analyzeDna(humanDna);

        // Assert
        assertFalse(result);
        verify(dnaRecordRepository, times(1)).findByDnaSequence(anyString());
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe manejar correctamente el formato de secuencia concatenada")
    void testDnaSequenceFormatting() {
        // Arrange
        String[] dna = {"ATGC", "CAGT", "TGAT", "GCAT"};
        String expectedSequence = "ATGC,CAGT,TGAT,GCAT";
        
        when(dnaRecordRepository.findByDnaSequence(expectedSequence))
            .thenReturn(Optional.empty());
        
        when(mutantDetector.isMutant(dna)).thenReturn(false);
        
        when(dnaRecordRepository.save(any(DnaRecord.class)))
            .thenReturn(new DnaRecord(dna, false));

        // Act
        mutantService.analyzeDna(dna);

        // Assert: Verificar que se buscó con el formato correcto
        verify(dnaRecordRepository, times(1)).findByDnaSequence(expectedSequence);
    }
}
