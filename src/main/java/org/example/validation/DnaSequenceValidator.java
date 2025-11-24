package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

/**
 * Implementación del validador custom para secuencias de ADN.
 * 
 * Valida:
 * 1. No null, no vacío
 * 2. Matriz NxN (cuadrada)
 * 3. Solo caracteres A, T, C, G
 * 4. Tamaño mínimo 4x4, máximo 1000x1000
 */
public class DnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');
    private static final int MIN_SIZE = 4;
    private static final int MAX_SIZE = 1000;

    @Override
    public void initialize(ValidDnaSequence constraintAnnotation) {
        // Inicialización si es necesaria
    }

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        // Null check - será manejado por @NotNull
        if (dna == null || dna.length == 0) {
            return false;
        }

        int n = dna.length;

        // Validar tamaño máximo
        if (n > MAX_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "La matriz de ADN es demasiado grande. Máximo permitido: " + MAX_SIZE + "x" + MAX_SIZE
            ).addConstraintViolation();
            return false;
        }

        // Validar tamaño mínimo
        if (n < MIN_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "La matriz de ADN debe ser al menos de " + MIN_SIZE + "x" + MIN_SIZE
            ).addConstraintViolation();
            return false;
        }

        // Validar que sea matriz NxN y solo contenga A, T, C, G
        for (String row : dna) {
            if (row == null || row.length() != n) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "La matriz de ADN debe ser NxN. Tamaño esperado: " + n + "x" + n
                ).addConstraintViolation();
                return false;
            }

            for (char base : row.toCharArray()) {
                if (!VALID_BASES.contains(base)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                        "ADN inválido: Solo se permiten caracteres A, T, C, G. Encontrado: '" + base + "'"
                    ).addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }
}
