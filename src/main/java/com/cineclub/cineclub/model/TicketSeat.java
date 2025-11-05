package com.cineclub.cineclub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_seat")
public class TicketSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id")
    private RoomSeat seat;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public RoomSeat getSeat() { return seat; }
    public void setSeat(RoomSeat seat) { this.seat = seat; }
}


