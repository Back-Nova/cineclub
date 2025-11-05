package com.cineclub.cineclub.controller;

import com.cineclub.cineclub.dto.MovieRequestDto;
import com.cineclub.cineclub.dto.MovieFilterDto;
import com.cineclub.cineclub.model.Movie;
import com.cineclub.cineclub.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
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
    
    @PostMapping("/search")
    public ResponseEntity<Page<Movie>> search(@RequestBody MovieFilterDto filtro,
                                              @PageableDefault(size = 10) Pageable pageable) {
        Page<Movie> movies = movieService.search(filtro, pageable);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Movie>> searchGet(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String sinopsis,
            @RequestParam(required = false) Integer duracionMinima,
            @RequestParam(required = false) Integer duracionMaxima,
            @RequestParam(required = false) String clasificacionEdad,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate fechaHasta,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        MovieFilterDto filtro = new MovieFilterDto();
        filtro.setTitulo(titulo);
        filtro.setSinopsis(sinopsis);
        filtro.setDuracionMinima(duracionMinima);
        filtro.setDuracionMaxima(duracionMaxima);
        filtro.setClasificacionEdad(clasificacionEdad);
        filtro.setFechaDesde(fechaDesde);
        filtro.setFechaHasta(fechaHasta);
        Page<Movie> movies = movieService.search(filtro, pageable);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/{id:\\d+}")
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

