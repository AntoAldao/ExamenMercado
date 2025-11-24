package org.example.service;

import org.example.entity.DnaRecord;
import org.example.exception.InvalidDnaException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para las nuevas funcionalidades de MutantService.
 * Tests para el método deleteDnaRecord.
 */
@ExtendWith(MockitoExtension.class)
class MutantServiceNewFeaturesTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    private String validHash;
    private DnaRecord mockRecord;

    @BeforeEach
    void setUp() {
        validHash = "3a5f2c9e8b1d4f7a6c3e9d2b8f5a1c7e4d9b2f6a8c3e5d1b7f4a9c2e6d8b3f5a1";
        mockRecord = new DnaRecord();
        mockRecord.setDnaHash(validHash);
        mockRecord.setIsMutant(true);
    }

    @Test
    @DisplayName("deleteDnaRecord debe eliminar registro existente")
    void testDeleteDnaRecord_ShouldDelete_WhenRecordExists() {
        // Arrange
        when(dnaRecordRepository.findByDnaHash(validHash)).thenReturn(Optional.of(mockRecord));
        doNothing().when(dnaRecordRepository).delete(mockRecord);

        // Act
        assertDoesNotThrow(() -> mutantService.deleteDnaRecord(validHash));

        // Assert
        verify(dnaRecordRepository).findByDnaHash(validHash);
        verify(dnaRecordRepository).delete(mockRecord);
    }

    @Test
    @DisplayName("deleteDnaRecord debe lanzar excepción cuando el registro no existe")
    void testDeleteDnaRecord_ShouldThrowException_WhenRecordNotFound() {
        // Arrange
        String nonExistentHash = "nonexistent123";
        when(dnaRecordRepository.findByDnaHash(nonExistentHash)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidDnaException exception = assertThrows(
            InvalidDnaException.class,
            () -> mutantService.deleteDnaRecord(nonExistentHash)
        );

        assertTrue(exception.getMessage().contains("No se encontró registro con hash"));
        verify(dnaRecordRepository).findByDnaHash(nonExistentHash);
        verify(dnaRecordRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteDnaRecord debe manejar hash null")
    void testDeleteDnaRecord_ShouldThrowException_WhenHashIsNull() {
        // Arrange
        when(dnaRecordRepository.findByDnaHash(null)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidDnaException.class, () -> mutantService.deleteDnaRecord(null));
    }
}
