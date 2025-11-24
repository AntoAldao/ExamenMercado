package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

/**
 * Servicio para calcular estadísticas de verificaciones de ADN.
 * 
 * Responsabilidades:
 * - Consultar contadores de mutantes y humanos
 * - Calcular el ratio mutantes/humanos
 * - Manejar casos especiales (división por cero)
 */
@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository dnaRecordRepository;

    /**
     * Obtiene las estadísticas de verificaciones realizadas.
     * 
     * @return StatsResponse con contadores y ratio
     */
    public StatsResponse getStats() {
        long mutantCount = dnaRecordRepository.countMutants();
        long humanCount = dnaRecordRepository.countHumans();
        
        // Calcular ratio, manejando división por cero
        double ratio = humanCount > 0 ? (double) mutantCount / humanCount : 0.0;
        
        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
