package com.cineclub.cineclub.dto;

import java.time.LocalDate;

public class MovieFilterDto {
    private String titulo;
    private String sinopsis;
    private Integer duracionMinima;
    private Integer duracionMaxima;
    private String clasificacionEdad;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

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

    public Integer getDuracionMinima() {
        return duracionMinima;
    }

    public void setDuracionMinima(Integer duracionMinima) {
        this.duracionMinima = duracionMinima;
    }

    public Integer getDuracionMaxima() {
        return duracionMaxima;
    }

    public void setDuracionMaxima(Integer duracionMaxima) {
        this.duracionMaxima = duracionMaxima;
    }

    public String getClasificacionEdad() {
        return clasificacionEdad;
    }

    public void setClasificacionEdad(String clasificacionEdad) {
        this.clasificacionEdad = clasificacionEdad;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}


