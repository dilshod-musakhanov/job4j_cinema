package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

@Service
public class SimpleTicketservice implements TicketService {

    private final TicketRepository ticketRepository;

    public SimpleTicketservice(TicketRepository sql2oTicketRepository) {
        this.ticketRepository = sql2oTicketRepository;
    }

    @Override
    public Optional<Ticket> createTicket(Ticket ticket) {
        return ticketRepository.createTicket(ticket);
    }
}
