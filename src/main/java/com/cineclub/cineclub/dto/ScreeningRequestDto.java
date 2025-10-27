package com.cineclub.cineclub.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ScreeningRequestDto {
    
    @NotNull(message = "El ID de la película es obligatorio")
    private Long peliculaId;
    
    @NotNull(message = "El ID de la sala es obligatorio")
    private Long salaId;
    
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalDateTime horaInicio;
    
    @NotNull(message = "La hora de fin es obligatoria")
    private LocalDateTime horaFin;
    
    private String estado;
    
    // Constructors
    public ScreeningRequestDto() {}
    
    public ScreeningRequestDto(Long peliculaId, Long salaId, LocalDateTime horaInicio, LocalDateTime horaFin, String estado) {
        this.peliculaId = peliculaId;
        this.salaId = salaId;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
    }
    
    // Getters and Setters
    public Long getPeliculaId() {
        return peliculaId;
    }
    
    public void setPeliculaId(Long peliculaId) {
        this.peliculaId = peliculaId;
    }
    
    public Long getSalaId() {
        return salaId;
    }
    
    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }
    
    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }
    
    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public LocalDateTime getHoraFin() {
        return horaFin;
    }
    
    public void setHoraFin(LocalDateTime horaFin) {
        this.horaFin = horaFin;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
}

