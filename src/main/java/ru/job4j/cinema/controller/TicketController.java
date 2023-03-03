package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final FilmSessionService filmSessionService;
    private final FilmService filmService;
    private final TicketService ticketService;

    public TicketController(FilmSessionService filmSessionService, FilmService filmService, TicketService ticketService) {
        this.filmSessionService = filmSessionService;
        this.filmService = filmService;
        this.ticketService = ticketService;
    }

    @GetMapping("/confirmTicket")
    public String confirmTicket(Model model, @ModelAttribute Ticket ticket) {
        if (ticket.getUserId() == 0) {
            model.addAttribute("message", "You have to log in first to buy ticket");
            return "errors/404";
        }
        var filmSession = filmSessionService.findById(ticket.getSessionId());
        var film = filmService.getByFilmId(filmSession.get().getFilmId());
        if (film.isEmpty()) {
            model.addAttribute("message", "This movie is not found");
            return "errors/404";
        }
        model.addAttribute("ticketSession", ticket.getSessionId());
        model.addAttribute("ticketUserId", ticket.getUserId());
        model.addAttribute("ticketFilmName", film.get().getName());
        model.addAttribute("ticketSessionStart", filmSession.get().getStartTime());
        model.addAttribute("ticketSessionEnd", filmSession.get().getEndTime());
        model.addAttribute("ticketRowNumber", ticket.getRowNumber());
        model.addAttribute("ticketPlaceNumber", ticket.getPlaceNumber());
        return "ticket/confirmTicket";
    }

    @PostMapping("/confirmAndBuyTicket")
    public String confirmAndBuyTicket(Model model, @ModelAttribute Ticket ticket) {
        Ticket newT = new Ticket();
        newT.setSessionId(ticket.getSessionId());
        newT.setRowNumber(ticket.getRowNumber());
        newT.setPlaceNumber(ticket.getPlaceNumber());
        newT.setUserId(ticket.getUserId());
        var createdTicket = ticketService.createTicket(newT);
        var filmSession = filmSessionService.findById(ticket.getSessionId());
        var film = filmService.getByFilmId(filmSession.get().getFilmId());
        if (createdTicket.isEmpty()) {
            model.addAttribute(
                    "message",
                    "Ticket is already sold out. Choose other seat or session");
            return "errors/404";
        }
        model.addAttribute(
                "message",
                "Ticket sent to your email. Enjoy watching " + film.get().getName());
        return "success/success";
    }
}
