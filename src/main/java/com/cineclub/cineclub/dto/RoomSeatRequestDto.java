package com.cineclub.cineclub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RoomSeatRequestDto {
    
    @NotNull(message = "El ID de la sala es obligatorio")
    private Long salaId;
    
    @NotBlank(message = "La fila es obligatoria")
    private String filaLabel;
    
    @NotNull(message = "El número de asiento es obligatorio")
    @Positive(message = "El número de asiento debe ser positivo")
    private Integer numeroAsiento;
    
    // Constructor
    public RoomSeatRequestDto() {}
    
    public RoomSeatRequestDto(Long salaId, String filaLabel, Integer numeroAsiento) {
        this.salaId = salaId;
        this.filaLabel = filaLabel;
        this.numeroAsiento = numeroAsiento;
    }
    
    
    public Long getSalaId() {
        return salaId;
    }
    
    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }
    
    public String getFilaLabel() {
        return filaLabel;
    }
    
    public void setFilaLabel(String filaLabel) {
        this.filaLabel = filaLabel;
    }
    
    public Integer getNumeroAsiento() {
        return numeroAsiento;
    }
    
    public void setNumeroAsiento(Integer numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }
}


