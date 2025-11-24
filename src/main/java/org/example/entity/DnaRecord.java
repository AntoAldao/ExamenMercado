package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA que representa un registro de ADN analizado.
 * 
 * Almacena:
 * - La secuencia de ADN en formato String (concatenada con comas)
 * - Si es mutante o no
 * - Timestamp de cuando se analizó
 */
@Entity
@Table(name = "dna_records", indexes = {
    @Index(name = "idx_dna_sequence", columnList = "dna_sequence", unique = true),
    @Index(name = "idx_is_mutant", columnList = "is_mutant")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Secuencia de ADN almacenada como String único.
     * Ejemplo: "ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG"
     */
    @Column(name = "dna_sequence", nullable = false, unique = true, length = 10000)
    private String dnaSequence;

    /**
     * Indica si la secuencia pertenece a un mutante.
     */
    @Column(name = "is_mutant", nullable = false)
    private Boolean isMutant;

    /**
     * Constructor personalizado para crear un registro desde un array de DNA.
     * 
     * @param dna Array de strings representando el ADN
     * @param isMutant Si es mutante o no
     */
    public DnaRecord(String[] dna, Boolean isMutant) {
        this.dnaSequence = String.join(",", dna);
        this.isMutant = isMutant;
    }
}
