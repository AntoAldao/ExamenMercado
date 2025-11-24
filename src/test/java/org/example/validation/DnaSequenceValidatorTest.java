package org.example.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para DnaSequenceValidator.
 * Verifica las nuevas validaciones: tamaño máximo 1000x1000.
 */
class DnaSequenceValidatorTest {

    private DnaSequenceValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new DnaSequenceValidator();
        context = mock(ConstraintValidatorContext.class);
        
        // Mock para builder de mensajes personalizados
        ConstraintValidatorContext.ConstraintViolationBuilder builder = 
            mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    @Test
    @DisplayName("Debe aceptar matriz 4x4 (tamaño mínimo)")
    void testValid_MinimumSize() {
        String[] dna = {
            "ATGC",
            "CAGT",
            "TTAT",
            "AGAC"
        };
        
        assertTrue(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Debe rechazar matriz 3x3 (menor al mínimo)")
    void testInvalid_BelowMinimumSize() {
        String[] dna = {
            "ATG",
            "CAG",
            "TTA"
        };
        
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Debe aceptar matriz 1000x1000 (tamaño máximo)")
    void testValid_MaximumSize() {
        int size = 1000;
        String[] dna = new String[size];
        String row = "A".repeat(size);
        
        for (int i = 0; i < size; i++) {
            dna[i] = row;
        }
        
        assertTrue(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Debe rechazar matriz 1001x1001 (mayor al máximo)")
    void testInvalid_AboveMaximumSize() {
        int size = 1001;
        String[] dna = new String[size];
        String row = "A".repeat(size);
        
        for (int i = 0; i < size; i++) {
            dna[i] = row;
        }
        
        assertFalse(validator.isValid(dna, context));
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(contains("demasiado grande"));
    }

    @Test
    @DisplayName("Debe aceptar matriz con solo caracteres válidos A, T, C, G")
    void testValid_OnlyValidCharacters() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        
        assertTrue(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Debe rechazar matriz con carácter inválido")
    void testInvalid_WithInvalidCharacter() {
        String[] dna = {
            "ATXCGA",  // X es inválido
            "CAGTGC",
            "TTATGT",
            "AGAAGG"
        };
        
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Debe rechazar matriz no cuadrada")
    void testInvalid_NonSquareMatrix() {
        String[] dna = {
            "ATGC",    // 4 caracteres
            "CAGT",    // 4 caracteres
            "TTAT",    // 4 caracteres
            "AGACC"    // 5 caracteres - no coincide
        };
        
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Debe rechazar DNA null")
    void testInvalid_NullDna() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    @DisplayName("Debe rechazar array vacío")
    void testInvalid_EmptyArray() {
        String[] dna = {};
        assertFalse(validator.isValid(dna, context));
    }
}
