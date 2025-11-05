package com.cineclub.cineclub.repository;

import com.cineclub.cineclub.model.TicketSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketSeatRepository extends JpaRepository<TicketSeat, Long> {
}


