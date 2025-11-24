package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Anotación de validación custom para secuencias de ADN.
 * 
 * Valida que:
 * - La matriz sea NxN (cuadrada)
 * - Solo contenga caracteres A, T, C, G
 * - No sea null ni vacía
 * 
 * Uso:
 * @ValidDnaSequence
 * private String[] dna;
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DnaSequenceValidator.class)
@Documented
public @interface ValidDnaSequence {
    
    String message() default "Secuencia de ADN inválida";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
