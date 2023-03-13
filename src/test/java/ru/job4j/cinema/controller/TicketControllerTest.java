package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class TicketControllerTest {

    private FilmSessionService filmSessionService;
    private FilmService filmService;
    private TicketService ticketService;
    private TicketController ticketController;

    @BeforeEach
    public void preLoad() {
        filmSessionService = mock(FilmSessionService.class);
        filmService = mock(FilmService.class);
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(filmSessionService, filmService, ticketService);
    }

    @Test
    public void whenRequestConfirmTicketThenGetTicketConfirmTicket() {
        Ticket ticket = new Ticket(
                1,
                2,
                3,
                4,
                5
        );
        Optional<FilmSession> filmSession = Optional.of(new FilmSession(
                2,
                1,
                3,
                "2023-03-15 21:00:00",
                "2023-03-15 23:00:00"
        ));
        Optional<Film> film = Optional.of(new Film(
                1,
                "It",
                "Scary movie",
                2016,
                2,
                18,
                130,
                3
                )
        );
        when(filmSessionService.findById(ticket.getSessionId())).thenReturn(filmSession);
        when(filmService.getByFilmId(filmSession.get().getFilmId())).thenReturn(film);
        var model = new ConcurrentModel();
        var view = ticketController.confirmTicket(model, ticket);
        assertThat(view).isEqualTo("ticket/confirmTicket");
        assertThat(model.getAttribute("ticketSession")).isEqualTo(ticket.getSessionId());
        assertThat(model.getAttribute("ticketUserId")).isEqualTo(ticket.getUserId());
        assertThat(model.getAttribute("ticketFilmName")).isEqualTo(film.get().getName());
        assertThat(model.getAttribute("ticketSessionStart")).isEqualTo(filmSession.get().getStartTime());
        assertThat(model.getAttribute("ticketSessionEnd")).isEqualTo(filmSession.get().getEndTime());
        assertThat(model.getAttribute("ticketRowNumber")).isEqualTo(ticket.getRowNumber());
        assertThat(model.getAttribute("ticketPlaceNumber")).isEqualTo(ticket.getPlaceNumber());
    }

    @Test
    public void whenRequestConfirmTicketWithoutLoginThenGetErrors404() {
        Ticket ticket = new Ticket(
                1,
                2,
                3,
                4,
                0
        );
        Optional<FilmSession> filmSession = Optional.of(new FilmSession(
                2,
                1,
                3,
                "2023-03-15 21:00:00",
                "2023-03-15 23:00:00"
        ));
        Optional<Film> film = Optional.of(new Film(
                1,
                "It",
                "Scary movie",
                2016,
                2,
                18,
                130,
                3
                )
        );
        when(filmSessionService.findById(ticket.getSessionId())).thenReturn(filmSession);
        when(filmService.getByFilmId(filmSession.get().getFilmId())).thenReturn(film);
        var model = new ConcurrentModel();
        var view = ticketController.confirmTicket(model, ticket);
        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("You have to log in first to buy ticket");
    }

    @Test
    public void whenPostConfirmAndBuyTicketThenGetSuccessTicketSuccess() {
        Ticket draftTicket = new Ticket(
                1,
                2,
                3,
                4,
                5
        );
        Ticket newTicket = new Ticket();
        newTicket.setSessionId(draftTicket.getSessionId());
        newTicket.setRowNumber(draftTicket.getRowNumber());
        newTicket.setPlaceNumber(draftTicket.getPlaceNumber());
        newTicket.setUserId(draftTicket.getUserId());
        Optional<FilmSession> filmSession = Optional.of(new FilmSession(
                2,
                1,
                3,
                "2023-03-15 21:00:00",
                "2023-03-15 23:00:00"
        ));
        Optional<Film> film = Optional.of(new Film(
                1,
                "It",
                "Scary movie",
                2016,
                2,
                18,
                130,
                3
                )
        );
        when(ticketService.createTicket(draftTicket)).thenReturn(Optional.of(newTicket));
        when(filmSessionService.findById(draftTicket.getSessionId())).thenReturn(filmSession);
        when(filmService.getByFilmId(filmSession.get().getFilmId())).thenReturn(film);
        var model = new ConcurrentModel();
        var view = ticketController.confirmAndBuyTicket(model, draftTicket);
        assertThat(view).isEqualTo("success/ticketSuccess");
        assertThat(model.getAttribute("message"))
                .isEqualTo("Ticket sent to your email. Enjoy watching " + film.get().getName());
    }

    @Test
    public void whenPostConfirmAndBuyTicketWithTakenSeatThenGetSuccessTicketSuccess() {
        Ticket draftTicket = new Ticket(
                1,
                2,
                3,
                4,
                5
        );
        Ticket newTicket = new Ticket();
        newTicket.setSessionId(draftTicket.getSessionId());
        newTicket.setRowNumber(draftTicket.getRowNumber());
        newTicket.setPlaceNumber(draftTicket.getPlaceNumber());
        newTicket.setUserId(draftTicket.getUserId());
        Optional<FilmSession> filmSession = Optional.of(new FilmSession(
                2,
                1,
                3,
                "2023-03-15 21:00:00",
                "2023-03-15 23:00:00"
        ));
        Optional<Film> film = Optional.of(new Film(
                        1,
                        "It",
                        "Scary movie",
                        2016,
                        2,
                        18,
                        130,
                        3
                )
        );
        when(ticketService.createTicket(draftTicket)).thenReturn(Optional.empty());
        when(filmSessionService.findById(draftTicket.getSessionId())).thenReturn(filmSession);
        when(filmService.getByFilmId(filmSession.get().getFilmId())).thenReturn(film);
        var model = new ConcurrentModel();
        var view = ticketController.confirmAndBuyTicket(model, draftTicket);
        assertThat(view).isEqualTo("errors/ticket404");
        assertThat(model.getAttribute("message"))
                .isEqualTo("Ticket is already sold out. Choose other seat or session");
    }
}
