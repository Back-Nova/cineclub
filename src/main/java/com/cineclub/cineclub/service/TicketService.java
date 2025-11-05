package com.cineclub.cineclub.service;

import com.cineclub.cineclub.exception.BusinessException;
import com.cineclub.cineclub.exception.ResourceNotFoundException;
import com.cineclub.cineclub.model.*;
import com.cineclub.cineclub.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TicketService {

    private final ScreeningRepository screeningRepository;
    private final RoomSeatRepository roomSeatRepository;
    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;

    private final int maxSeatsPerUserPerScreening = 6;
    private final int defaultTtlSeconds = 300;
    private final int minTtlSeconds = 60;
    private final int maxTtlSeconds = 900;
    private final int minMinutesBeforeStart = 10;

    public TicketService(ScreeningRepository screeningRepository,
                         RoomSeatRepository roomSeatRepository,
                         TicketRepository ticketRepository,
                         TicketSeatRepository ticketSeatRepository) {
        this.screeningRepository = screeningRepository;
        this.roomSeatRepository = roomSeatRepository;
        this.ticketRepository = ticketRepository;
        this.ticketSeatRepository = ticketSeatRepository;
    }

    @Transactional
    public Ticket createHold(Long userId, Long screeningId, List<Long> seatIds, Integer ttlSeconds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new BusinessException("Debe indicar al menos una butaca");
        }

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ResourceNotFoundException("Función no encontrada"));

        if (screening.getHoraInicio().isBefore(LocalDateTime.now().plusMinutes(minMinutesBeforeStart))) {
            throw new BusinessException("No se permiten reservas a pocos minutos del inicio");
        }

        int ttl = ttlSeconds != null ? ttlSeconds : defaultTtlSeconds;
        if (ttl < minTtlSeconds || ttl > maxTtlSeconds) {
            throw new BusinessException("TTL fuera de rango permitido");
        }

        // Limite por usuario
        List<Ticket> userHolds = ticketRepository.findByUserId(userId, Pageable.unpaged()).getContent();
        long activeHoldsSeats = userHolds.stream()
                .filter(t -> t.getStatus() == TicketStatus.HOLD_ACTIVE && t.getExpiresAt() != null && t.getExpiresAt().isAfter(LocalDateTime.now()))
                .flatMap(t -> t.getSeats().stream()).count();
        if (activeHoldsSeats + seatIds.size() > maxSeatsPerUserPerScreening) {
            throw new BusinessException("Supera el límite de butacas en hold por usuario");
        }

        // Validar disponibilidad por cada asiento
        Set<Long> uniqueSeatIds = new HashSet<>(seatIds);
        if (uniqueSeatIds.size() != seatIds.size()) {
            throw new BusinessException("Lista de asientos contiene duplicados");
        }

        List<RoomSeat> seats = roomSeatRepository.findAllById(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new BusinessException("Alguna butaca no existe");
        }

        // Verificar que no esté VENDIDA
        List<Ticket> sold = ticketRepository.findByScreeningIdAndStatus(screeningId, TicketStatus.SOLD);
        Set<Long> soldSeatIds = new HashSet<>();
        sold.forEach(t -> t.getSeats().forEach(ts -> soldSeatIds.add(ts.getSeat().getId())));
        for (Long sid : seatIds) {
            if (soldSeatIds.contains(sid)) {
                throw new BusinessException("Butaca ya vendida: " + sid);
            }
        }

        // Verificar que no esté RESERVADA por otro usuario (hold activo)
        List<Ticket> holds = ticketRepository.findByScreeningIdAndStatus(screeningId, TicketStatus.HOLD_ACTIVE);
        Set<Long> heldSeatIds = new HashSet<>();
        LocalDateTime now = LocalDateTime.now();
        holds.forEach(t -> {
            if (t.getExpiresAt() != null && t.getExpiresAt().isAfter(now)) {
                t.getSeats().forEach(ts -> heldSeatIds.add(ts.getSeat().getId()));
            }
        });
        for (Long sid : seatIds) {
            if (heldSeatIds.contains(sid)) {
                throw new BusinessException("Butaca en hold por otro usuario: " + sid);
            }
        }

        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setScreening(screening);
        ticket.setStatus(TicketStatus.HOLD_ACTIVE);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setExpiresAt(LocalDateTime.now().plusSeconds(ttl));
        Ticket saved = ticketRepository.save(ticket);

        for (RoomSeat seat : seats) {
            TicketSeat ts = new TicketSeat();
            ts.setTicket(saved);
            ts.setSeat(seat);
            ticketSeatRepository.save(ts);
            saved.getSeats().add(ts);
        }

        return saved;
    }

    @Transactional
    public Ticket confirm(Long userId, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado"));
        if (!ticket.getUserId().equals(userId)) {
            throw new BusinessException("No es propietario del ticket");
        }
        if (ticket.getStatus() != TicketStatus.HOLD_ACTIVE) {
            return ticket; // idempotente
        }
        if (ticket.getExpiresAt() == null || ticket.getExpiresAt().isBefore(LocalDateTime.now())) {
            ticket.setStatus(TicketStatus.HOLD_EXPIRED);
            return ticketRepository.save(ticket);
        }
        ticket.setStatus(TicketStatus.SOLD);
        ticket.setPurchasedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket cancel(Long userId, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado"));
        if (!ticket.getUserId().equals(userId)) {
            throw new BusinessException("No es propietario del ticket");
        }
        if (ticket.getStatus() == TicketStatus.HOLD_ACTIVE) {
            ticket.setStatus(TicketStatus.HOLD_CANCELLED);
            return ticketRepository.save(ticket);
        }
        throw new BusinessException("Estado no permite cancelación");
    }

    @Transactional(readOnly = true)
    public Page<Ticket> findMyTickets(Long userId, Pageable pageable) {
        return ticketRepository.findByUserId(userId, pageable);
    }
}


