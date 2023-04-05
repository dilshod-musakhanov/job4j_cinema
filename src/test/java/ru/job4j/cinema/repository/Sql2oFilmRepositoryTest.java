package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DatasourceConfiguration;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;

import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository filmRepository;
    private static Sql2oFileRepository fileRepository;
    private static Sql2oGenreRepository genreRepository;
    private static File file;
    private static Genre genre;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFilmRepositoryTest.class.
                getClassLoader().
                getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        filmRepository = new Sql2oFilmRepository(sql2o);
        fileRepository = new Sql2oFileRepository(sql2o);
        genreRepository = new Sql2oGenreRepository(sql2o);

        file = new File(1, "testFile", "testFile");
        fileRepository.addFile(file);

        genre = new Genre(1, "History");
        genreRepository.addGenre(genre);

        var films = filmRepository.findAll();
        for (var film : films) {
            filmRepository.deleteByFilmId(film.getId());
        }

    }

    @AfterAll
    public static void deleteFile() {
        fileRepository.deleteByFileId(file.getId());
        genreRepository.deleteByGenreId(genre.getId());
    }

    @AfterEach
    public void clearFilms() {
        var films = filmRepository.findAll();
        for (var film : films) {
            filmRepository.deleteByFilmId(film.getId());
        }
    }

    @Test
    public void whenAddThenGetSame() {
        var film = filmRepository.addFilm(new Film(
                1,
                "Forest Gump",
                "Good movie",
                1987,
                genre.getId(),
                18,
                120,
                file.getId())
        );
        var addedFilm = filmRepository.findByFilmId(film.get().getId());
        assertThat(addedFilm).usingRecursiveComparison().isEqualTo(film);
    }

    @Test
    public void whenAddSeveralThenFindAll() {
        var film1 = filmRepository.addFilm(new Film(
                1,
                "Forest Gump",
                "Good movie",
                1987,
                genre.getId(),
                18,
                120,
                file.getId())
        );
        var film2 = filmRepository.addFilm(new Film(
                2,
                "Global Warming",
                "Good movie",
                2000,
                genre.getId(),
                18,
                120,
                file.getId())
        );
        var films = filmRepository.findAll();
        assertThat(films).usingRecursiveComparison().isEqualTo(List.of(film1.get(), film2.get()));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var film = filmRepository.addFilm(new Film(
                1,
                "Forest Gump",
                "Good movie",
                1987,
                genre.getId(),
                18,
                120,
                file.getId())
        );
        var isDeleted = filmRepository.deleteByFilmId(film.get().getId());
        var empty = filmRepository.findByFilmId(film.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(empty).usingRecursiveComparison().isEqualTo(empty());
    }

    @Test
    public void whenAddNothingThenGetEmptyList() {
        var empty = filmRepository.findAll();
        assertThat(empty).usingRecursiveComparison().isEqualTo(emptyList());
    }

    @Test
    public void whenDeleteWithInvalidIdThenGetFalse() {
        var result = filmRepository.deleteByFilmId(0);
        assertThat(result).isFalse();
    }
}
