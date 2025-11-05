package com.cineclub.cineclub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class MovieRequestDto {
    
    @NotBlank(message = "El ttulo es obligatorio")
    private String titulo;
    
    private String sinopsis;
    
    @NotNull(message = "La durac es obligatoria")
    @Positive(message = "La duracn debe ser positiva")
    private Integer duracionMinutos;
    
    private String clasificacionEdad;
    
    private LocalDate fechaEstreno;
    
    // 
    public MovieRequestDto() {}
    
    public MovieRequestDto(String titulo, String sinopsis, Integer duracionMinutos, String clasificacionEdad, LocalDate fechaEstreno) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.duracionMinutos = duracionMinutos;
        this.clasificacionEdad = clasificacionEdad;
        this.fechaEstreno = fechaEstreno;
    }
    
    // 
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getSinopsis() {
        return sinopsis;
    }
    
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }
    
    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }
    
    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
    
    public String getClasificacionEdad() {
        return clasificacionEdad;
    }
    
    public void setClasificacionEdad(String clasificacionEdad) {
        this.clasificacionEdad = clasificacionEdad;
    }
    
    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }
    
    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }
}

