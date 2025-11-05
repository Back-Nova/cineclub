package com.cineclub.cineclub.service;

import com.cineclub.cineclub.model.*;
import com.cineclub.cineclub.repository.RoomSeatRepository;
import com.cineclub.cineclub.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AvailabilityService {

    private final RoomSeatRepository roomSeatRepository;
    private final TicketRepository ticketRepository;

    public AvailabilityService(RoomSeatRepository roomSeatRepository, TicketRepository ticketRepository) {
        this.roomSeatRepository = roomSeatRepository;
        this.ticketRepository = ticketRepository;
    }

    public Map<Long, String> getSeatStates(Long screeningId, Long roomId) {
        List<RoomSeat> seats = roomSeatRepository.findBySalaId(roomId);

        Map<Long, String> stateBySeatId = new HashMap<>();
        for (RoomSeat seat : seats) {
            stateBySeatId.put(seat.getId(), "DISPONIBLE");
        }

        // Marcar VENDIDA
        List<Ticket> soldTickets = ticketRepository.findByScreeningIdAndStatus(screeningId, TicketStatus.SOLD);
        soldTickets.forEach(t -> t.getSeats().forEach(ts -> stateBySeatId.put(ts.getSeat().getId(), "VENDIDA")));

        // Marcar RESERVADA si no está VENDIDA y el hold está activo (no vencido)
        List<Ticket> holds = ticketRepository.findByScreeningIdAndStatus(screeningId, TicketStatus.HOLD_ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        holds.forEach(t -> {
            if (t.getExpiresAt() != null && t.getExpiresAt().isAfter(now)) {
                t.getSeats().forEach(ts -> stateBySeatId.compute(ts.getSeat().getId(), (k, v) -> "VENDIDA".equals(v) ? v : "RESERVADA"));
            }
        });

        return stateBySeatId;
    }
}


