package com.cineclub.cineclub.controller;

import com.cineclub.cineclub.dto.RoomRequestDto;
import com.cineclub.cineclub.model.Room;
import com.cineclub.cineclub.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    
    private final RoomService roomService;
    
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    
    @PostMapping
    public ResponseEntity<Room> create(@Valid @RequestBody RoomRequestDto dto) {
        Room room = roomService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }
    
    @GetMapping
    public ResponseEntity<Page<Room>> findAll(
            @RequestParam(required = false) String nombre,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Room> rooms;
        
        if (nombre != null && !nombre.trim().isEmpty()) {
            rooms = roomService.search(nombre, pageable);
        } else {
            rooms = roomService.findAll(pageable);
        }
        
        return ResponseEntity.ok(rooms);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable Long id) {
        Room room = roomService.findById(id);
        return ResponseEntity.ok(room);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable Long id, @Valid @RequestBody RoomRequestDto dto) {
        Room room = roomService.update(id, dto);
        return ResponseEntity.ok(room);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

