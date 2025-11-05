package com.cineclub.cineclub.service;

import com.cineclub.cineclub.dto.RoomRequestDto;
import com.cineclub.cineclub.exception.ResourceNotFoundException;
import com.cineclub.cineclub.model.Room;
import com.cineclub.cineclub.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {
    
    private final RoomRepository roomRepository;
    
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    @Transactional
    public Room create(RoomRequestDto dto) {
        // Verificar nombres duplicados
        roomRepository.findByNombre(dto.getNombre())
            .ifPresent(room -> {
                throw new IllegalArgumentException("Ya existe una sala con el nombre: " + dto.getNombre());
            });
        
        Room room = new Room();
        room.setNombre(dto.getNombre());
        room.setCapacidad(dto.getCapacidad());
        
        return roomRepository.save(room);
    }
    
    @Transactional(readOnly = true)
    public Page<Room> findAll(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Room> search(String nombre, Pageable pageable) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return roomRepository.findAll(pageable);
        }
        return roomRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }
    
    @Transactional(readOnly = true)
    public Room findById(Long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + id));
    }
    
    @Transactional
    public Room update(Long id, RoomRequestDto dto) {
        Room room = findById(id);
        
        // Verificar nombres duplicados si el nombre fue cambiado
        if (!room.getNombre().equals(dto.getNombre())) {
            roomRepository.findByNombre(dto.getNombre())
                .ifPresent(r -> {
                    throw new IllegalArgumentException("Ya existe una sala con el nombre: " + dto.getNombre());
                });
        }
        
        room.setNombre(dto.getNombre());
        room.setCapacidad(dto.getCapacidad());
        
        return roomRepository.save(room);
    }
    
    @Transactional
    public void delete(Long id) {
        Room room = findById(id);
        roomRepository.delete(room);
    }
}

