package com.cineclub.cineclub.repository;

import com.cineclub.cineclub.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    Page<Room> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    
    Optional<Room> findByNombre(String nombre);
}

