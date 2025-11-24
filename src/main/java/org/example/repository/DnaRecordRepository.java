package org.example.repository;

import org.example.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para gestionar registros de ADN.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas para:
 * - Buscar ADN por secuencia
 * - Contar mutantes y humanos
 */
@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    /**
     * Busca un registro de ADN por su secuencia exacta.
     * Permite evitar duplicados y hacer caché de resultados previos.
     * 
     * @param dnaSequence Secuencia de ADN concatenada (ej: "ATGCGA,CAGTGC,...")
     * @return Optional con el registro si existe
     */
    Optional<DnaRecord> findByDnaSequence(String dnaSequence);

    /**
     * Cuenta cuántos registros de ADN mutante existen.
     * 
     * @return Cantidad de mutantes detectados
     */
    @Query("SELECT COUNT(d) FROM DnaRecord d WHERE d.isMutant = true")
    long countMutants();

    /**
     * Cuenta cuántos registros de ADN humano existen.
     * 
     * @return Cantidad de humanos detectados
     */
    @Query("SELECT COUNT(d) FROM DnaRecord d WHERE d.isMutant = false")
    long countHumans();
}
