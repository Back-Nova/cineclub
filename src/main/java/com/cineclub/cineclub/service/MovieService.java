package com.cineclub.cineclub.service;

import com.cineclub.cineclub.dto.MovieRequestDto;
import com.cineclub.cineclub.dto.MovieFilterDto;
import com.cineclub.cineclub.exception.ResourceNotFoundException;
import com.cineclub.cineclub.model.Movie;
import com.cineclub.cineclub.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cineclub.cineclub.specification.MovieSpecifications;

@Service
public class MovieService {
    
    private final MovieRepository movieRepository;
    
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    
    @Transactional
    public Movie create(MovieRequestDto dto) {
        // comprueba titllos duplicados
        movieRepository.findByTitulo(dto.getTitulo())
            .ifPresent(movie -> {
                throw new IllegalArgumentException("ya existe la peli con ese titulo: " + dto.getTitulo());
            });
        
        Movie movie = new Movie();
        movie.setTitulo(dto.getTitulo());
        movie.setSinopsis(dto.getSinopsis());
        movie.setDuracionMinutos(dto.getDuracionMinutos());
        movie.setClasificacionEdad(dto.getClasificacionEdad());
        movie.setFechaEstreno(dto.getFechaEstreno());
        
        return movieRepository.save(movie);
    }
    
    @Transactional(readOnly = true)
    public Page<Movie> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Movie> search(String titulo, Pageable pageable) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return movieRepository.findAll(pageable);
        }
        return movieRepository.searchMovies(titulo, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Movie> search(MovieFilterDto filtro, Pageable pageable) {
        Specification<Movie> spec = MovieSpecifications.buildFromDto(filtro);
        return movieRepository.findAll(spec, pageable);
    }
    
    @Transactional(readOnly = true)
    public Movie findById(Long id) {
        return movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("movie no encontrada con id: " + id));
    }
    
    @Transactional
    public Movie update(Long id, MovieRequestDto dto) {
        Movie movie = findById(id);
        
        // Comprueba titulo duplicado 
        if (!movie.getTitulo().equals(dto.getTitulo())) {
            movieRepository.findByTitulo(dto.getTitulo())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("Ya existe una película con el título: " + dto.getTitulo());
                });
        }
        
        movie.setTitulo(dto.getTitulo());
        movie.setSinopsis(dto.getSinopsis());
        movie.setDuracionMinutos(dto.getDuracionMinutos());
        movie.setClasificacionEdad(dto.getClasificacionEdad());
        movie.setFechaEstreno(dto.getFechaEstreno());
        
        return movieRepository.save(movie);
    }
    
    @Transactional
    public void delete(Long id) {
        Movie movie = findById(id);
        movieRepository.delete(movie);
    }
}

