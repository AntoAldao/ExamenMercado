package org.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.ValidDnaSequence;

/**
 * DTO para recibir la secuencia de ADN en el endpoint /mutant/.
 * 
 * Ejemplo de JSON esperado:
 * {
 *   "dna": ["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {

    /**
     * Array de Strings que representa cada fila de una matriz (NxN) de ADN.
     * Cada string contiene solo los caracteres: A, T, C, G.
     */
    @NotNull(message = "La secuencia de ADN no puede ser null")
    @NotEmpty(message = "La secuencia de ADN no puede estar vacía")
    @ValidDnaSequence
    private String[] dna;
}
