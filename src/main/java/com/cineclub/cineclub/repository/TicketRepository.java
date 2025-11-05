package com.cineclub.cineclub.repository;

import com.cineclub.cineclub.model.Ticket;
import com.cineclub.cineclub.model.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    List<Ticket> findByScreeningIdAndStatus(Long screeningId, TicketStatus status);

    List<Ticket> findByStatusAndExpiresAtBefore(TicketStatus status, LocalDateTime now);
}


