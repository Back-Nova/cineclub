package com.cineclub.cineclub.repository;

import com.cineclub.cineclub.model.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    
    Page<Screening> findByPeliculaId(Long peliculaId, Pageable pageable);
    
    Page<Screening> findBySalaId(Long salaId, Pageable pageable);
    
    @Query("SELECT s FROM Screening s WHERE s.horaInicio >= :start AND s.horaInicio <= :end")
    Page<Screening> findByFechaRange(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        Pageable pageable
    );
    
    // Check for overlapping screenings in the same room
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Screening s " +
           "WHERE s.sala.id = :salaId " +
           "AND s.id != :screeningId " +
           "AND ((s.horaInicio <= :horaFin AND s.horaFin >= :horaInicio))")
    boolean existsOverlappingScreening(
        @Param("salaId") Long salaId,
        @Param("screeningId") Long screeningId,
        @Param("horaInicio") LocalDateTime horaInicio,
        @Param("horaFin") LocalDateTime horaFin
    );
    
    // For new screenings (no id)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Screening s " +
           "WHERE s.sala.id = :salaId " +
           "AND ((s.horaInicio <= :horaFin AND s.horaFin >= :horaInicio))")
    boolean existsOverlappingScreeningNew(
        @Param("salaId") Long salaId,
        @Param("horaInicio") LocalDateTime horaInicio,
        @Param("horaFin") LocalDateTime horaFin
    );
    
    List<Screening> findByEstado(String estado);
}

