package org.example.repository;

import org.example.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    /**
     * Busca un registro por su hash SHA-256.
     * Permite identificar ADN único de forma eficiente.
     * 
     * @param dnaHash Hash SHA-256 de la secuencia de ADN
     * @return Optional con el registro si existe
     */
    Optional<DnaRecord> findByDnaHash(String dnaHash);

    /**
     * Elimina un registro por su hash SHA-256.
     * 
     * @param dnaHash Hash SHA-256 de la secuencia de ADN
     */
    void deleteByDnaHash(String dnaHash);

    /**
     * Cuenta cuántos registros de ADN mutante existen en un rango de fechas.
     * 
     * @param startDate Fecha de inicio (incluida)
     * @param endDate Fecha de fin (incluida)
     * @return Cantidad de mutantes detectados en el período
     */
    @Query("SELECT COUNT(d) FROM DnaRecord d WHERE d.isMutant = true " +
           "AND (:startDate IS NULL OR d.createdAt >= :startDate) " +
           "AND (:endDate IS NULL OR d.createdAt <= :endDate)")
    long countMutantsByDateRange(@Param("startDate") LocalDate startDate, 
                                  @Param("endDate") LocalDate endDate);

    /**
     * Cuenta cuántos registros de ADN humano existen en un rango de fechas.
     * 
     * @param startDate Fecha de inicio (incluida)
     * @param endDate Fecha de fin (incluida)
     * @return Cantidad de humanos detectados en el período
     */
    @Query("SELECT COUNT(d) FROM DnaRecord d WHERE d.isMutant = false " +
           "AND (:startDate IS NULL OR d.createdAt >= :startDate) " +
           "AND (:endDate IS NULL OR d.createdAt <= :endDate)")
    long countHumansByDateRange(@Param("startDate") LocalDate startDate, 
                                 @Param("endDate") LocalDate endDate);
}
