package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> addTicket(Ticket ticket);
    Collection<Ticket> findAll();
    Optional<Ticket> findByTicketId(int id);
    boolean deleteTicketById(int id);

}
