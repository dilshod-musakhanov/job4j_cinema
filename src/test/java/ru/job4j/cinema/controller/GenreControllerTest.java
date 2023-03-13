package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.service.GenreService;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class GenreControllerTest {

    private GenreService genreService;
    private GenreController genreController;

    @BeforeEach
    public void preLoad() {
        genreService = mock(GenreService.class);
        genreController = new GenreController(genreService);
    }

    @Test
    public void whenRequestGenresThenGetGenreGenres() {
        Collection<Genre> genres = List.of(
                new Genre(1, "comedy"),
                new Genre(2, "horror")
        );
        when(genreService.findAll()).thenReturn(genres);
        var model = new ConcurrentModel();
        var view = genreController.genres(model);
        assertThat(view).isEqualTo("genre/genres");
        assertThat(model.getAttribute("genres")).isEqualTo(genres);
    }
}
