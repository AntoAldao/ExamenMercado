package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para responder con las estadísticas en el endpoint /stats.
 * 
 * Ejemplo de JSON de respuesta:
 * {
 *   "count_mutant_dna": 40,
 *   "count_human_dna": 100,
 *   "ratio": 0.4
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {

    /**
     * Cantidad de ADN mutantes detectados.
     */
    @JsonProperty("count_mutant_dna")
    private long countMutantDna;

    /**
     * Cantidad de ADN humanos detectados.
     */
    @JsonProperty("count_human_dna")
    private long countHumanDna;

    /**
     * Ratio entre mutantes y humanos.
     * Fórmula: count_mutant_dna / count_human_dna
     */
    @JsonProperty("ratio")
    private double ratio;
}
