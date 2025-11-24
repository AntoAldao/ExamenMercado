package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

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
    @Index(name = "idx_is_mutant", columnList = "is_mutant"),
    @Index(name = "idx_dna_hash", columnList = "dna_hash", unique = true)
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
     * Hash SHA-256 de la secuencia de ADN.
     * Permite identificar de forma única y eficiente cada secuencia.
     */
    @Column(name = "dna_hash", nullable = false, unique = true, length = 64)
    private String dnaHash;

    /**
     * Indica si la secuencia pertenece a un mutante.
     */
    @Column(name = "is_mutant", nullable = false)
    private Boolean isMutant;

    /**
     * Timestamp de cuando se realizó el análisis.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Hook de JPA para establecer createdAt antes de persistir.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor personalizado para crear un registro desde un array de DNA.
     * 
     * @param dna Array de strings representando el ADN
     * @param isMutant Si es mutante o no
     */
    public DnaRecord(String[] dna, Boolean isMutant) {
        this.dnaSequence = String.join(",", dna);
        this.dnaHash = calculateHash(this.dnaSequence);
        this.isMutant = isMutant;
    }

    /**
     * Calcula el hash SHA-256 de una secuencia de ADN.
     * 
     * @param dnaSequence Secuencia de ADN concatenada
     * @return Hash SHA-256 en formato hexadecimal
     */
    private String calculateHash(String dnaSequence) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dnaSequence.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular hash SHA-256", e);
        }
    }
}
