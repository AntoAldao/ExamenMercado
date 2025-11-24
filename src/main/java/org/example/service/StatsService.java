package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    /**
     * Obtiene las estadísticas filtradas por rango de fechas.
     * 
     * @param startDate Fecha de inicio (opcional)
     * @param endDate Fecha de fin (opcional)
     * @return StatsResponse con contadores y ratio del período especificado
     */
    public StatsResponse getStatsByDateRange(LocalDate startDate, LocalDate endDate) {
        long mutantCount = dnaRecordRepository.countMutantsByDateRange(startDate, endDate);
        long humanCount = dnaRecordRepository.countHumansByDateRange(startDate, endDate);
        
        // Calcular ratio, manejando división por cero
        double ratio = humanCount > 0 ? (double) mutantCount / humanCount : 0.0;
        
        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
