package com.cineclub.cineclub.service;

import com.cineclub.cineclub.dto.MovieRequestDto;
import com.cineclub.cineclub.exception.ResourceNotFoundException;
import com.cineclub.cineclub.model.Movie;
import com.cineclub.cineclub.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {
    
    private final MovieRepository movieRepository;
    
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    
    @Transactional
    public Movie create(MovieRequestDto dto) {
        // Check for duplicate titles
        movieRepository.findByTitulo(dto.getTitulo())
            .ifPresent(movie -> {
                throw new IllegalArgumentException("Ya existe una pelicula con el titulo: " + dto.getTitulo());
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
    public Movie findById(Long id) {
        return movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("pelicula no encontrada : " + id));
    }
    
    @Transactional
    public Movie update(Long id, MovieRequestDto dto) {
        Movie movie = findById(id);
        
        // Check for duplicate titles if title changed
        if (!movie.getTitulo().equals(dto.getTitulo())) {
            movieRepository.findByTitulo(dto.getTitulo())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("ya existe la pelicula con ese titulo : " + dto.getTitulo());
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

