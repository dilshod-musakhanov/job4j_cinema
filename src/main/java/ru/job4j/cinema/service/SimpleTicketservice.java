package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleTicketservice implements TicketService {

    private final TicketRepository ticketRepository;

    public SimpleTicketservice(TicketRepository sql2oTicketRepository) {
        this.ticketRepository = sql2oTicketRepository;
    }

    @Override
    public Optional<Ticket> addTicket(Ticket ticket) {
        return ticketRepository.addTicket(ticket);
    }

    @Override
    public Collection<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Optional<Ticket> findByTicketId(int id) {
        return ticketRepository.findByTicketId(id);
    }

    @Override
    public boolean deleteTicketById(int id) {
        return ticketRepository.deleteTicketById(id);
    }
}
