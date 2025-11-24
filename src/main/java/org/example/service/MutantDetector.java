package org.example.service;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Servicio que contiene el algoritmo optimizado de detección de mutantes.
 * 
 * OPTIMIZACIONES IMPLEMENTADAS:
 * 1. Early Termination - Retorna inmediatamente al encontrar >1 secuencia
 * 2. Conversión a char[][] - Acceso O(1) más rápido que String.charAt()
 * 3. Boundary Checking - Solo busca donde cabe la secuencia
 * 4. Direct Comparison - Comparaciones sin loops adicionales
 * 5. Validation Set O(1) - Validación eficiente de caracteres
 * 
 * COMPLEJIDAD:
 * - Temporal: O(N²) en el peor caso, ~O(N) con early termination en mutantes
 * - Espacial: O(1) - Solo usa variables locales y contador
 * 
 * @author MercadoLibre Backend Exam
 */
@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    /**
     * Determina si una secuencia de ADN pertenece a un mutante.
     * 
     * Un mutante tiene MÁS DE UNA secuencia de 4 letras iguales consecutivas,
     * ya sea horizontal, vertical o diagonal.
     * 
     * @param dna Array de Strings que representa la matriz de ADN (NxN)
     * @return true si es mutante (2+ secuencias), false si es humano (0-1 secuencias)
     * @throws IllegalArgumentException si el ADN es inválido
     */
    public boolean isMutant(String[] dna) {
        validateDna(dna);
        
        int n = dna.length;
        
        // Optimización: Convertir a char[][] para acceso O(1) más rápido
        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }
        
        int sequenceCount = 0;
        
        // Single Pass: Un solo recorrido de la matriz
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                
                // Boundary Checking: Solo buscar si cabe la secuencia
                
                // Horizontal (→)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        // Early Termination: Retornar inmediatamente si >1 secuencia
                        if (sequenceCount > 1) {
                            return true;
                        }
                    }
                }
                
                // Vertical (↓)
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) {
                            return true;
                        }
                    }
                }
                
                // Diagonal principal (↘)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDown(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) {
                            return true;
                        }
                    }
                }
                
                // Diagonal inversa (↙)
                if (row <= n - SEQUENCE_LENGTH && col >= SEQUENCE_LENGTH - 1) {
                    if (checkDiagonalUp(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    /**
     * Verifica secuencia horizontal (→).
     * Direct Comparison: Sin loops, comparaciones directas.
     */
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
               matrix[row][col + 2] == base &&
               matrix[row][col + 3] == base;
    }

    /**
     * Verifica secuencia vertical (↓).
     */
    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
               matrix[row + 2][col] == base &&
               matrix[row + 3][col] == base;
    }

    /**
     * Verifica secuencia diagonal hacia abajo (↘).
     */
    private boolean checkDiagonalDown(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
               matrix[row + 2][col + 2] == base &&
               matrix[row + 3][col + 3] == base;
    }

    /**
     * Verifica secuencia diagonal hacia arriba (↙).
     */
    private boolean checkDiagonalUp(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col - 1] == base &&
               matrix[row + 2][col - 2] == base &&
               matrix[row + 3][col - 3] == base;
    }

    /**
     * Valida que el ADN cumpla con las reglas:
     * - No null, no vacío
     * - Matriz NxN (cuadrada)
     * - Solo caracteres A, T, C, G
     */
    private void validateDna(String[] dna) {
        if (dna == null || dna.length == 0) {
            throw new IllegalArgumentException("La secuencia de ADN no puede ser null o vacía");
        }
        
        int n = dna.length;
        
        for (String row : dna) {
            if (row == null || row.length() != n) {
                throw new IllegalArgumentException("La matriz de ADN debe ser NxN");
            }

            if (n < SEQUENCE_LENGTH) {
                throw new IllegalArgumentException("La matriz de ADN debe ser al menos de 4x4");
            }
            
            // Validation Set O(1): Verificar caracteres válidos
            for (char base : row.toCharArray()) {
                if (!VALID_BASES.contains(base)) {
                    throw new IllegalArgumentException(
                        "ADN inválido: Solo se permiten caracteres A, T, C, G. Encontrado: " + base
                    );
                }
            }
        }
    }
}
