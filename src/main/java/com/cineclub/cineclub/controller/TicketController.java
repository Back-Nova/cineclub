package com.cineclub.cineclub.controller;

import com.cineclub.cineclub.model.Ticket;
import com.cineclub.cineclub.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    private Long resolveUserId(Map<String, String> headers) {
        String userHeader = headers.getOrDefault("x-user-id", null);
        if (userHeader == null) {
            throw new IllegalArgumentException("X-User-Id requerido para operaciones de tickets");
        }
        return Long.parseLong(userHeader);
    }

    @PostMapping("/api/screenings/{screeningId}/holds")
    public ResponseEntity<Ticket> createHold(@PathVariable Long screeningId,
                                             @RequestBody CreateHoldRequest request,
                                             @RequestHeader Map<String, String> headers) {
        Long userId = resolveUserId(headers);
        Ticket ticket = ticketService.createHold(userId, screeningId, request.seatIds(), request.ttlSeconds());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @PostMapping("/api/tickets/{ticketId}/confirm")
    public ResponseEntity<Ticket> confirm(@PathVariable Long ticketId,
                                          @RequestHeader Map<String, String> headers) {
        Long userId = resolveUserId(headers);
        Ticket ticket = ticketService.confirm(userId, ticketId);
        return ResponseEntity.ok(ticket);
    }

    @DeleteMapping("/api/tickets/{ticketId}")
    public ResponseEntity<Ticket> cancel(@PathVariable Long ticketId,
                                         @RequestHeader Map<String, String> headers) {
        Long userId = resolveUserId(headers);
        Ticket ticket = ticketService.cancel(userId, ticketId);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/api/me/tickets")
    public ResponseEntity<Page<Ticket>> myTickets(@RequestHeader Map<String, String> headers,
                                                  @PageableDefault(size = 10) Pageable pageable) {
        Long userId = resolveUserId(headers);
        Page<Ticket> page = ticketService.findMyTickets(userId, pageable);
        return ResponseEntity.ok(page);
    }

    public record CreateHoldRequest(List<Long> seatIds, Integer ttlSeconds) {}
}


