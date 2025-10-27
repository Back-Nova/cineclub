package com.cineclub.cineclub.controller;

import com.cineclub.cineclub.dto.RoomSeatRequestDto;
import com.cineclub.cineclub.model.RoomSeat;
import com.cineclub.cineclub.service.RoomSeatService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms/{roomId}/seats")
public class RoomSeatController {
    
    private final RoomSeatService roomSeatService;
    
    public RoomSeatController(RoomSeatService roomSeatService) {
        this.roomSeatService = roomSeatService;
    }
    
    @PostMapping
    public ResponseEntity<RoomSeat> create(@PathVariable Long roomId, @Valid @RequestBody RoomSeatRequestDto dto) {
        dto.setSalaId(roomId);
        RoomSeat seat = roomSeatService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(seat);
    }
    
    @GetMapping
    public ResponseEntity<Page<RoomSeat>> findAll(@PathVariable Long roomId, 
                                                   @PageableDefault(size = 100) Pageable pageable) {
        Page<RoomSeat> seats = roomSeatService.findBySala(roomId, pageable);
        return ResponseEntity.ok(seats);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<RoomSeat>> findAllByRoom(@PathVariable Long roomId) {
        List<RoomSeat> seats = roomSeatService.findAllBySala(roomId);
        return ResponseEntity.ok(seats);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RoomSeat> findById(@PathVariable Long roomId, @PathVariable Long id) {
        RoomSeat seat = roomSeatService.findById(id);
        return ResponseEntity.ok(seat);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RoomSeat> update(@PathVariable Long roomId, @PathVariable Long id, 
                                            @Valid @RequestBody RoomSeatRequestDto dto) {
        dto.setSalaId(roomId);
        RoomSeat seat = roomSeatService.update(id, dto);
        return ResponseEntity.ok(seat);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long roomId, @PathVariable Long id) {
        roomSeatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

