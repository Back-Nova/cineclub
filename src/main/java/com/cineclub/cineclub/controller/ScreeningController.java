package com.cineclub.cineclub.controller;

import com.cineclub.cineclub.dto.ScreeningRequestDto;
import com.cineclub.cineclub.model.Screening;
import com.cineclub.cineclub.service.ScreeningService;
import com.cineclub.cineclub.service.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {
    
    private final ScreeningService screeningService;
    private final AvailabilityService availabilityService;
    
    public ScreeningController(ScreeningService screeningService, AvailabilityService availabilityService) {
        this.screeningService = screeningService;
        this.availabilityService = availabilityService;
    }
    
    @PostMapping
    public ResponseEntity<Screening> create(@Valid @RequestBody ScreeningRequestDto dto) {
        Screening screening = screeningService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(screening);
    }
    
    @GetMapping
    public ResponseEntity<Page<Screening>> findAll(
            @RequestParam(required = false) Long peliculaId,
            @RequestParam(required = false) Long salaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PageableDefault(size = 10) Pageable pageable) {
        
        Page<Screening> screenings;
        
        if (peliculaId != null) {
            screenings = screeningService.findByMovie(peliculaId, pageable);
        } else if (salaId != null) {
            screenings = screeningService.findByRoom(salaId, pageable);
        } else if (start != null && end != null) {
            screenings = screeningService.findByDateRange(start, end, pageable);
        } else {
            screenings = screeningService.findAll(pageable);
        }
        
        return ResponseEntity.ok(screenings);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Screening> findById(@PathVariable Long id) {
        Screening screening = screeningService.findById(id);
        return ResponseEntity.ok(screening);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Screening> update(@PathVariable Long id, @Valid @RequestBody ScreeningRequestDto dto) {
        Screening screening = screeningService.update(id, dto);
        return ResponseEntity.ok(screening);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        screeningService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<Long, String>> availability(@PathVariable Long id) {
        Screening screening = screeningService.findById(id);
        Map<Long, String> states = availabilityService.getSeatStates(id, screening.getSala().getId());
        return ResponseEntity.ok(states);
    }
}

