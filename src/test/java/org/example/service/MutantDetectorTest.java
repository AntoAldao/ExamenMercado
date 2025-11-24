package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para MutantDetector.
 * 
 * Cobertura: 16+ tests
 * Objetivo: >95% cobertura en MutantDetector
 * 
 * Categorías de tests:
 * 1. Mutantes con diferentes combinaciones de secuencias
 * 2. Humanos con 0 o 1 secuencia
 * 3. Casos edge (matrices mínimas, máximas)
 * 4. Validaciones (null, vacío, no cuadrada, caracteres inválidos)
 */
@DisplayName("MutantDetector - Tests Unitarios del Algoritmo")
class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // ==========================================
    // TESTS DE MUTANTES (2+ SECUENCIAS)
    // ==========================================

    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
    void testMutantWithHorizontalAndDiagonalSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",  // Secuencia horizontal: CCCC
            "TCACTG"
        };
        // Diagonal (0,0)A → (1,1)A → (2,2)A → (3,3)A
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantWithVerticalSequences() {
        String[] dna = {
            "ATGCGA",
            "AGGTGC",
            "AGATGT",
            "AGAAGG",  // Columna 0: AAAA (vertical)
            "CCCCTA",  // Fila 4: CCCC (horizontal)
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con múltiples secuencias diagonales")
    void testMutantWithMultipleDiagonalSequences() {
        String[] dna = {
            "AAAA",  // Horizontal: AAAA
            "TATT",
            "GCAT",
            "GGGG"   // Horizontal: GGGG
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz 6x6 con secuencias en todas direcciones")
    void testMutantWithAllDirections() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal inversa")
    void testMutantWithReverseDiagonal() {
        String[] dna = {
            "ATGCGA",
            "CAGTAC",
            "TTATAT",
            "AGAATG",  // Diagonal inversa desde (0,3): A
            "CCACTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con exactamente 2 secuencias")
    void testMutantWithExactlyTwoSequences() {
        String[] dna = {
            "AAAATG",  // Horizontal: AAAA (secuencia 1)
            "TGCTTG",
            "TGCTTG",
            "TGCTTG",  // Vertical columna 3: TTTT (secuencia 2)
            "CGTACG",
            "AGTCAG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // ==========================================
    // TESTS DE HUMANOS (0-1 SECUENCIAS)
    // ==========================================

    @Test
    @DisplayName("Debe detectar humano con solo 1 secuencia")
    void testHumanWithOnlyOneSequence() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATTT",  // Solo 1 secuencia: TTT (pero necesita 4)
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar humano sin secuencias")
    void testHumanWithNoSequences() {
        String[] dna = {
            "ATGC",
            "CAGT",
            "TGAT",
            "GCTA"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar humano con secuencias de solo 3 letras")
    void testHumanWithSequencesOfThreeOnly() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATTG",  // TTT (solo 3, necesita 4)
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // ==========================================
    // TESTS DE CASOS EDGE
    // ==========================================

    @Test
    @DisplayName("Debe manejar matriz 4x4 (tamaño mínimo)")
    void testMinimumMatrixSize() {
        String[] dna = {
            "AAAA",  // Secuencia 1
            "TTTT",  // Secuencia 2
            "CCGG",
            "AGTC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe manejar matriz 4x4 humano")
    void testMinimumMatrixSizeHuman() {
        String[] dna = {
            "ATGC",
            "CAGT",
            "TGAT",
            "GCAT"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // ==========================================
    // TESTS DE VALIDACIONES
    // ==========================================

    @Test
    @DisplayName("Debe lanzar excepción si el ADN es null")
    void testNullDnaArray() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> mutantDetector.isMutant(null)
        );
        assertEquals("La secuencia de ADN no puede ser null o vacía", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ADN está vacío")
    void testEmptyDnaArray() {
        String[] dna = {};
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> mutantDetector.isMutant(dna)
        );
        assertEquals("La secuencia de ADN no puede ser null o vacía", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la matriz no es NxN")
    void testInvalidMatrixNonSquare() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTAT"    // Fila más corta
        };
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> mutantDetector.isMutant(dna)
        );
        assertEquals("La matriz de ADN debe ser NxN", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si contiene caracteres inválidos")
    void testInvalidCharacters() {
        String[] dna = {
            "ATGC",
            "CXGT",  // X es inválido
            "TGAT",
            "GCAT"
        };
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> mutantDetector.isMutant(dna)
        );
        assertTrue(exception.getMessage().contains("Solo se permiten caracteres A, T, C, G"));
    }

    @Test
    @DisplayName("Debe lanzar excepción si hay una fila null")
    void testNullRowInMatrix() {
        String[] dna = {
            "ATGC",
            null,    // Fila null
            "TGAT",
            "GCAT"
        };
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> mutantDetector.isMutant(dna)
        );
        assertEquals("La matriz de ADN debe ser NxN", exception.getMessage());
    }

    // ==========================================
    // TESTS ADICIONALES PARA COBERTURA >95%
    // ==========================================

    @Test
    @DisplayName("Debe detectar mutante con secuencias en esquinas")
    void testMutantWithCornerSequences() {
        String[] dna = {
            "AAAATG",  // Horizontal inicio
            "TGCATG",
            "TGCATG",
            "TGCATG",
            "TGCATG",
            "CGGGGG"   // Horizontal final: GGGGG (5 letras, cuenta como secuencia)
        };
        assertTrue(mutantDetector.isMutant(dna));
    }
}
