package com.cineclub.cineclub.service;

import com.cineclub.cineclub.dto.MovieRequestDto;
import com.cineclub.cineclub.exception.ResourceNotFoundException;
import com.cineclub.cineclub.model.Movie;
import com.cineclub.cineclub.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    
    @Mock
    private MovieRepository movieRepository;
    
    @InjectMocks
    private MovieService movieService;
    
    private Movie movie;
    private MovieRequestDto dto;
    
    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitulo("Test Movie");
        movie.setSinopsis("Test Synopsis");
        movie.setDuracionMinutos(120);
        movie.setClasificacionEdad("PG-13");
        movie.setFechaEstreno(LocalDate.of(2024, 1, 1));
        
        dto = new MovieRequestDto();
        dto.setTitulo("Test Movie");
        dto.setSinopsis("Test Synopsis");
        dto.setDuracionMinutos(120);
        dto.setClasificacionEdad("PG-13");
        dto.setFechaEstreno(LocalDate.of(2024, 1, 1));
    }
    
    @Test
    void testCreateMovie_Success() {
        when(movieRepository.findByTitulo(anyString())).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        
        Movie result = movieService.create(dto);
        
        assertNotNull(result);
        assertEquals("Test Movie", result.getTitulo());
        verify(movieRepository).findByTitulo(anyString());
        verify(movieRepository).save(any(Movie.class));
    }
    
    @Test
    void testCreateMovie_DuplicateTitle() {
        when(movieRepository.findByTitulo(anyString())).thenReturn(Optional.of(movie));
        
        assertThrows(IllegalArgumentException.class, () -> movieService.create(dto));
        verify(movieRepository, never()).save(any(Movie.class));
    }
    
    @Test
    void testFindAll() {
        Page<Movie> page = new PageImpl<>(Arrays.asList(movie));
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        Page<Movie> result = movieService.findAll(Pageable.unpaged());
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(movieRepository).findAll(any(Pageable.class));
    }
    
    @Test
    void testFindById_Success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        
        Movie result = movieService.findById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(movieRepository).findById(1L);
    }
    
    @Test
    void testFindById_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> movieService.findById(1L));
    }
    
    @Test
    void testUpdate_Success() {
        MovieRequestDto updateDto = new MovieRequestDto();
        updateDto.setTitulo("Updated Title");
        updateDto.setSinopsis("Updated Synopsis");
        updateDto.setDuracionMinutos(130);
        
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.findByTitulo(anyString())).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        
        Movie result = movieService.update(1L, updateDto);
        
        assertNotNull(result);
        verify(movieRepository).findById(1L);
        verify(movieRepository).save(any(Movie.class));
    }
    
    @Test
    void testDelete_Success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        doNothing().when(movieRepository).delete(any(Movie.class));
        
        movieService.delete(1L);
        
        verify(movieRepository).findById(1L);
        verify(movieRepository).delete(movie);
    }
}

