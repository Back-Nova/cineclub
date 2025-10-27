package com.cineclub.cineclub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "asiento_sala")
public class RoomSeat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La sala es obligatoria")
    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Room sala;
    
    @NotBlank(message = "La fila es obligatoria")
    @Column(nullable = false, length = 10)
    private String filaLabel;
    
    @NotNull(message = "El número de asiento es obligatorio")
    @Positive(message = "El número de asiento debe ser positivo")
    @Column(nullable = false)
    private Integer numeroAsiento;
    
    // Constructors
    public RoomSeat() {}
    
    public RoomSeat(Room sala, String filaLabel, Integer numeroAsiento) {
        this.sala = sala;
        this.filaLabel = filaLabel;
        this.numeroAsiento = numeroAsiento;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Room getSala() {
        return sala;
    }
    
    public void setSala(Room sala) {
        this.sala = sala;
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

