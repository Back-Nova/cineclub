package com.cineclub.cineclub.repository;

import com.cineclub.cineclub.model.RoomSeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomSeatRepository extends JpaRepository<RoomSeat, Long> {
    
    Page<RoomSeat> findBySalaId(Long salaId, Pageable pageable);
    
    List<RoomSeat> findBySalaId(Long salaId);
    
    @Query("SELECT rs FROM RoomSeat rs WHERE rs.sala.id = :salaId " +
           "AND rs.filaLabel = :filaLabel AND rs.numeroAsiento = :numeroAsiento")
    Optional<RoomSeat> findBySalaAndSeatNumber(
        @Param("salaId") Long salaId,
        @Param("filaLabel") String filaLabel,
        @Param("numeroAsiento") Integer numeroAsiento
    );
}

