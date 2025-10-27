package com.cineclub.cineclub.controller;

import com.cineclub.cineclub.dto.MovieRequestDto;
import com.cineclub.cineclub.model.Movie;
import com.cineclub.cineclub.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    
    private final MovieService movieService;
    
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    
    @PostMapping
    public ResponseEntity<Movie> create(@Valid @RequestBody MovieRequestDto dto) {
        Movie movie = movieService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }
    
    @GetMapping
    public ResponseEntity<Page<Movie>> findAll(
            @RequestParam(required = false) String titulo,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Movie> movies;
        
        if (titulo != null && !titulo.trim().isEmpty()) {
            movies = movieService.search(titulo, pageable);
        } else {
            movies = movieService.findAll(pageable);
        }
        
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Movie> findById(@PathVariable Long id) {
        Movie movie = movieService.findById(id);
        return ResponseEntity.ok(movie);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable Long id, @Valid @RequestBody MovieRequestDto dto) {
        Movie movie = movieService.update(id, dto);
        return ResponseEntity.ok(movie);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

