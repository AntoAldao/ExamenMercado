package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.exception.InvalidDnaException;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para gestionar la detección de mutantes.
 * 
 * Responsabilidades:
 * - Coordinar entre MutantDetector y DnaRecordRepository
 * - Implementar caché consultando registros existentes
 * - Guardar nuevos resultados en BD
 * 
 * Patrón: Service Layer + Repository Pattern
 */
@Service
@RequiredArgsConstructor
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    /**
     * Analiza una secuencia de ADN y determina si es mutante.
     * 
     * OPTIMIZACIÓN: Implementa caché verificando si el ADN ya fue analizado.
     * Si existe en BD, retorna el resultado previo sin re-analizar.
     * 
     * @param dna Array de strings representando la matriz de ADN
     * @return true si es mutante, false si es humano
     */
    @Transactional
    public boolean analyzeDna(String[] dna) {
        String dnaSequence = String.join(",", dna);
        
        // Caché: Verificar si ya existe en BD
        return dnaRecordRepository.findByDnaSequence(dnaSequence)
            .map(DnaRecord::getIsMutant)
            .orElseGet(() -> {
                // No existe: Analizar y guardar
                boolean isMutant = mutantDetector.isMutant(dna);
                DnaRecord record = new DnaRecord(dna, isMutant);
                dnaRecordRepository.save(record);
                return isMutant;
            });
    }

    /**
     * Elimina un registro de ADN por su hash SHA-256.
     * 
     * @param dnaHash Hash SHA-256 del ADN a eliminar
     * @throws InvalidDnaException si el registro no existe
     */
    @Transactional
    public void deleteDnaRecord(String dnaHash) {
        DnaRecord record = dnaRecordRepository.findByDnaHash(dnaHash)
            .orElseThrow(() -> new InvalidDnaException("No se encontró registro con hash: " + dnaHash));
        
        dnaRecordRepository.delete(record);
    }
}
