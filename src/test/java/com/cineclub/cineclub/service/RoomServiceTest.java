package com.cineclub.cineclub.service;

import com.cineclub.cineclub.dto.RoomRequestDto;
import com.cineclub.cineclub.exception.ResourceNotFoundException;
import com.cineclub.cineclub.model.Room;
import com.cineclub.cineclub.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    
    @Mock
    private RoomRepository roomRepository;
    
    @InjectMocks
    private RoomService roomService;
    
    private Room room;
    private RoomRequestDto dto;
    
    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setNombre("Sala 1");
        room.setCapacidad(100);
        
        dto = new RoomRequestDto();
        dto.setNombre("Sala 1");
        dto.setCapacidad(100);
    }
    
    @Test
    void testCreateRoom_Success() {
        when(roomRepository.findByNombre(anyString())).thenReturn(Optional.empty());
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        
        Room result = roomService.create(dto);
        
        assertNotNull(result);
        assertEquals("Sala 1", result.getNombre());
        verify(roomRepository).findByNombre(anyString());
        verify(roomRepository).save(any(Room.class));
    }
    
    @Test
    void testCreateRoom_DuplicateName() {
        when(roomRepository.findByNombre(anyString())).thenReturn(Optional.of(room));
        
        assertThrows(IllegalArgumentException.class, () -> roomService.create(dto));
        verify(roomRepository, never()).save(any(Room.class));
    }
    
    @Test
    void testFindById_NotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> roomService.findById(1L));
    }
    
    @Test
    void testDelete_Success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        doNothing().when(roomRepository).delete(any(Room.class));
        
        roomService.delete(1L);
        
        verify(roomRepository).findById(1L);
        verify(roomRepository).delete(room);
    }
}

