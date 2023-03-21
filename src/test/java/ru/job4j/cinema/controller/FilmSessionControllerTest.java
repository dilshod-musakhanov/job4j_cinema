package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.time.LocalTime.now;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class FilmSessionControllerTest {

    private FilmSessionService filmSessionService;
    private FilmService filmService;
    private HallService hallService;
    private FilmSessionController filmSessionController;
    private static LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private static LocalDateTime endTime = startTime.plusMinutes(90).truncatedTo(ChronoUnit.MINUTES);

    @BeforeEach
    public void preLoad() {
        filmSessionService = mock(FilmSessionService.class);
        filmService = mock(FilmService.class);
        hallService = mock(HallService.class);
        filmSessionController = new FilmSessionController(filmSessionService, filmService, hallService);
    }

    @Test
    public void whenRequestGetAllFilmSessionDto() {
        Collection<FilmSessionDto> filmSessionDtos = List.of(
            new FilmSessionDto(
                    1,
                    "It",
                    "SKY",
                    startTime,
                    endTime
                    ),
            new FilmSessionDto(
                    2,
                    "The Hangover",
                    "ISLAND",
                    startTime,
                    endTime
            )
        );
        when(filmSessionService.findAllFilmSessionDto()).thenReturn(filmSessionDtos);
        var model = new ConcurrentModel();
        var view = filmSessionController.getAllFilmSessionDto(model);
        assertThat(view).isEqualTo("filmSession/filmSessions");
        assertThat(model.getAttribute("filmSessions")).isEqualTo(filmSessionDtos);
    }

    @Test
    public void whenRequestFilmSessionByIdThenGetTicketBuyTicket() {
        Optional<FilmSession> filmSession = Optional.of(new FilmSession(
                1,
                2,
                3,
                startTime,
                endTime
                )
        );
        Optional<Film> film = Optional.of(new Film(
                2,
                "It",
                "Scary movie",
                2016,
                2,
                18,
                130,
                3
                )
        );
        Collection<Integer> rows = List.of(
                1,
                2,
                3
        );
        Collection<Integer> places = List.of(
                1,
                2,
                3,
                4,
                5
        );
        when(filmSessionService.findByFilmSessionId(filmSession.get().getId())).thenReturn(filmSession);
        when(filmService.findByFilmId(filmSession.get().getFilmId())).thenReturn(film);
        when(hallService.getRowCountByHallId(filmSession.get().getHallsId())).thenReturn(rows);
        when(hallService.getPlacesCountByHallId(filmSession.get().getHallsId())).thenReturn(places);
        var model = new ConcurrentModel();
        var view = filmSessionController.findById(model, 1);
        assertThat(view).isEqualTo("ticket/buyTicket");
        assertThat(model.getAttribute("sessionId")).isEqualTo(filmSession.get().getId());
        assertThat(model.getAttribute("rows")).isEqualTo(rows);
        assertThat(model.getAttribute("places")).isEqualTo(places);
        assertThat(model.getAttribute("filmName")).isEqualTo(film.get().getName());
        assertThat(model.getAttribute("filmId")).isEqualTo(film.get().getId());
    }

    @Test
    public void whenRequestFilmSessionByIdButFilmSessionIsNotAvailableThenGetErrors404() {
        Optional<FilmSession> filmSession = Optional.of(new FilmSession(
                1,
                2,
                3,
                startTime,
                endTime
                )
        );
        Optional<Film> film = Optional.of(new Film(
                        2,
                        "It",
                        "Scary movie",
                        2016,
                        2,
                        18,
                        130,
                        3
                )
        );
        Collection<Integer> rows = List.of(
                1,
                2,
                3
        );
        Collection<Integer> places = List.of(
                1,
                2,
                3,
                4,
                5
        );
        when(filmSessionService.findByFilmSessionId(filmSession.get().getId())).thenReturn(Optional.empty());
        when(filmService.findByFilmId(filmSession.get().getFilmId())).thenReturn(film);
        when(hallService.getRowCountByHallId(filmSession.get().getHallsId())).thenReturn(rows);
        when(hallService.getPlacesCountByHallId(filmSession.get().getHallsId())).thenReturn(places);
        var model = new ConcurrentModel();
        var view = filmSessionController.findById(model, 1);
        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("This film session is no longer available");
    }

}
