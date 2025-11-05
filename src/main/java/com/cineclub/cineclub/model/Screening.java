package com.cineclub.cineclub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "funcion")
public class Screening {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La película es obligatoria")
    @ManyToOne
    @JoinColumn(name = "pelicula_id", nullable = false)
    private Movie pelicula;
    
    @NotNull(message = "La sala es obligatoria")
    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Room sala;
    
    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDateTime horaInicio;
    
    @NotNull(message = "La hora de fin es obligatoria")
    @Column(nullable = false)
    private LocalDateTime horaFin;
    
    @Column(length = 20)
    private String estado;
    
    @PrePersist
    public void prePersist() {
        if (this.estado == null) {
            this.estado = "ACTIVA";
        }
    }
    
    // Constructor
    public Screening() {}
    
    public Screening(Movie pelicula, Room sala, LocalDateTime horaInicio, LocalDateTime horaFin, String estado) {
        this.pelicula = pelicula;
        this.sala = sala;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Movie getPelicula() {
        return pelicula;
    }
    
    public void setPelicula(Movie pelicula) {
        this.pelicula = pelicula;
    }
    
    public Room getSala() {
        return sala;
    }
    
    public void setSala(Room sala) {
        this.sala = sala;
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

