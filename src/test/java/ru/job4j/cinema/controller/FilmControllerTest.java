package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.service.FilmService;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class FilmControllerTest {

    private FilmService filmService;
    private FilmController filmController;

    @BeforeEach
    public void preLoad() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenRequestFilmsThenGetFilFilms() {
        Collection<FilmDto> filmDtos = List.of(
                new FilmDto(
                        1,
                        "It",
                        "Scary movie",
                        2020,
                        new Genre(2, "horror"),
                        18,
                        90,
                        1
                ),
                new FilmDto(
                        2,
                        "The Hangover",
                        "Funny movie",
                        2019,
                        new Genre(2, "comedy"),
                        18,
                        100,
                        2
                )
        );
        when(filmService.findAllFilmDto()).thenReturn(filmDtos);
        var model = new ConcurrentModel();
        var view = filmController.getAllFilmDto(model);
        assertThat(view).isEqualTo("film/films");
        assertThat(model.getAttribute("films")).isEqualTo(filmDtos);
    }
}
