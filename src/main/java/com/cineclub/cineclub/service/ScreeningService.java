package com.cineclub.cineclub.service;

import com.cineclub.cineclub.dto.ScreeningRequestDto;
import com.cineclub.cineclub.exception.BusinessException;
import com.cineclub.cineclub.exception.ResourceNotFoundException;
import com.cineclub.cineclub.model.Movie;
import com.cineclub.cineclub.model.Room;
import com.cineclub.cineclub.model.Screening;
import com.cineclub.cineclub.repository.MovieRepository;
import com.cineclub.cineclub.repository.RoomRepository;
import com.cineclub.cineclub.repository.ScreeningRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ScreeningService {
    
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    
    public ScreeningService(ScreeningRepository screeningRepository, MovieRepository movieRepository, 
                           RoomRepository roomRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }
    
    @Transactional
    public Screening create(ScreeningRequestDto dto) {
        // Validate dates
        if (dto.getHoraFin().isBefore(dto.getHoraInicio()) || dto.getHoraFin().isEqual(dto.getHoraInicio())) {
            throw new BusinessException("La hora de fin debe ser posterior a la hora de inicio");
        }
        
        // Validate movie exists
        Movie movie = movieRepository.findById(dto.getPeliculaId())
            .orElseThrow(() -> new ResourceNotFoundException("Película no encontrada con id: " + dto.getPeliculaId()));
        
        // Validate room exists
        Room room = roomRepository.findById(dto.getSalaId())
            .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + dto.getSalaId()));
        
        // Check for overlapping screenings in the same room
        boolean hasOverlap = screeningRepository.existsOverlappingScreeningNew(
            dto.getSalaId(),
            dto.getHoraInicio(),
            dto.getHoraFin()
        );
        
        if (hasOverlap) {
            throw new BusinessException("Existe un conflicto de horario con otra función en esta sala");
        }
        
        Screening screening = new Screening();
        screening.setPelicula(movie);
        screening.setSala(room);
        screening.setHoraInicio(dto.getHoraInicio());
        screening.setHoraFin(dto.getHoraFin());
        if (dto.getEstado() != null) {
            screening.setEstado(dto.getEstado());
        }
        
        return screeningRepository.save(screening);
    }
    
    @Transactional(readOnly = true)
    public Page<Screening> findAll(Pageable pageable) {
        return screeningRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Screening> findByMovie(Long peliculaId, Pageable pageable) {
        return screeningRepository.findByPeliculaId(peliculaId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Screening> findByRoom(Long salaId, Pageable pageable) {
        return screeningRepository.findBySalaId(salaId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Screening> findByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return screeningRepository.findByFechaRange(start, end, pageable);
    }
    
    @Transactional(readOnly = true)
    public Screening findById(Long id) {
        return screeningRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Funcion no encontrada con id: " + id));
    }
    
    @Transactional
    public Screening update(Long id, ScreeningRequestDto dto) {
        Screening screening = findById(id);
        
        // Validate dates
        if (dto.getHoraFin().isBefore(dto.getHoraInicio()) || dto.getHoraFin().isEqual(dto.getHoraInicio())) {
            throw new BusinessException("La hora de fin debe ser posterior a la hora de inicio");
        }
        
        // Validate movie exists if changed
        if (!screening.getPelicula().getId().equals(dto.getPeliculaId())) {
            Movie movie = movieRepository.findById(dto.getPeliculaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pelicula no encontrada con id: " + dto.getPeliculaId()));
            screening.setPelicula(movie);
        }
        
        // Validate room exists if changed
        if (!screening.getSala().getId().equals(dto.getSalaId())) {
            Room room = roomRepository.findById(dto.getSalaId())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + dto.getSalaId()));
            screening.setSala(room);
        }
        
        // Check for overlapping screenings in the same room
        boolean hasOverlap = screeningRepository.existsOverlappingScreening(
            dto.getSalaId(),
            id,
            dto.getHoraInicio(),
            dto.getHoraFin()
        );
        
        if (hasOverlap) {
            throw new BusinessException("Existe un conflicto de horario con otra función en esta sala");
        }
        
        screening.setHoraInicio(dto.getHoraInicio());
        screening.setHoraFin(dto.getHoraFin());
        if (dto.getEstado() != null) {
            screening.setEstado(dto.getEstado());
        }
        
        return screeningRepository.save(screening);
    }
    
    @Transactional
    public void delete(Long id) {
        Screening screening = findById(id);
        screeningRepository.delete(screening);
    }
}

