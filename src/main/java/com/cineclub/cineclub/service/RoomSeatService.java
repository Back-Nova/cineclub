package com.cineclub.cineclub.service;

import com.cineclub.cineclub.dto.RoomSeatRequestDto;
import com.cineclub.cineclub.exception.ResourceNotFoundException;
import com.cineclub.cineclub.model.Room;
import com.cineclub.cineclub.model.RoomSeat;
import com.cineclub.cineclub.repository.RoomRepository;
import com.cineclub.cineclub.repository.RoomSeatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomSeatService {
    
    private final RoomSeatRepository roomSeatRepository;
    private final RoomRepository roomRepository;
    
    public RoomSeatService(RoomSeatRepository roomSeatRepository, RoomRepository roomRepository) {
        this.roomSeatRepository = roomSeatRepository;
        this.roomRepository = roomRepository;
    }
    
    @Transactional
    public RoomSeat create(RoomSeatRequestDto dto) {
        // Validar que la sala exista
        Room room = roomRepository.findById(dto.getSalaId())
            .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + dto.getSalaId()));
        
        // Verificar que no exista un asiento duplicado
        roomSeatRepository.findBySalaAndSeatNumber(dto.getSalaId(), dto.getFilaLabel(), dto.getNumeroAsiento())
            .ifPresent(seat -> {
                throw new IllegalArgumentException("Ya existe un asiento " + dto.getFilaLabel() + 
                    dto.getNumeroAsiento() + " en esta sala");
            });
        
        RoomSeat seat = new RoomSeat();
        seat.setSala(room);
        seat.setFilaLabel(dto.getFilaLabel());
        seat.setNumeroAsiento(dto.getNumeroAsiento());
        
        return roomSeatRepository.save(seat);
    }
    
    @Transactional(readOnly = true)
    public Page<RoomSeat> findBySala(Long salaId, Pageable pageable) {
        return roomSeatRepository.findBySalaId(salaId, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<RoomSeat> findAllBySala(Long salaId) {
        return roomSeatRepository.findBySalaId(salaId);
    }
    
    @Transactional(readOnly = true)
    public RoomSeat findById(Long id) {
        return roomSeatRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asiento no encontrado con id: " + id));
    }
    
    @Transactional
    public RoomSeat update(Long id, RoomSeatRequestDto dto) {
        RoomSeat seat = findById(id);
        
        // Validar que la sala exista si fue cambiada
        if (!seat.getSala().getId().equals(dto.getSalaId())) {
            Room room = roomRepository.findById(dto.getSalaId())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + dto.getSalaId()));
            seat.setSala(room);
        }
        
        // Verificar que no exista un asiento duplicado
        roomSeatRepository.findBySalaAndSeatNumber(dto.getSalaId(), dto.getFilaLabel(), dto.getNumeroAsiento())
            .ifPresent(s -> {
                if (!s.getId().equals(id)) {
                    throw new IllegalArgumentException("Ya exste un asiento " + dto.getFilaLabel() +
                        dto.getNumeroAsiento() + " en esta sala");
                }
            });
        
        seat.setFilaLabel(dto.getFilaLabel());
        seat.setNumeroAsiento(dto.getNumeroAsiento());
        
        return roomSeatRepository.save(seat);
    }
    
    @Transactional
    public void delete(Long id) {
        RoomSeat seat = findById(id);
        roomSeatRepository.delete(seat);
    }
}

