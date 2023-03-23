package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oGenreRepositoryTest {

    private static Sql2oGenreRepository genreRepository;
    private static Sql2oFilmRepository filmRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        var properties = new Properties();
        try (var inStream = Sql2oGenreRepositoryTest.class
                .getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        genreRepository = new Sql2oGenreRepository(sql2o);
        filmRepository = new Sql2oFilmRepository(sql2o);
    }

    @AfterAll
    public static void deleteRep() {
        var films = filmRepository.findAll();
        for (var film : films) {
            filmRepository.deleteByFilmId(film.getId());
        }
    }

    @AfterEach
    public void clearGenre() {
        var genres = genreRepository.findAll();
        for (var genre : genres) {
            genreRepository.deleteByGenreId(genre.getId());
        }
    }

    @Test
    public void whenAddGenreGetSame() {
        var genre = genreRepository.addGenre(
                new Genre(1, "Adventure")
        );
        var addedGenre = genreRepository.findByGenreId(genre.get().getId());
        assertThat(addedGenre).usingRecursiveComparison().isEqualTo(genre);
    }

    @Test
    public void whenAddSeveralThenFindAll() {
        Genre genre1 = new Genre(1, "Action");
        Genre genre2 = new Genre(2, "Drama");
        genreRepository.addGenre(genre1);
        genreRepository.addGenre(genre2);
        var genres = genreRepository.findAll();
        assertThat(genres).usingRecursiveComparison().isEqualTo(List.of(genre1, genre2));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var genre = genreRepository.addGenre(new Genre(1, "Action"));
        var isDeleted = genreRepository.deleteByGenreId(genre.get().getId());
        var empty = genreRepository.findByGenreId(genre.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(empty).isEqualTo(empty());
    }

    @Test
    public void whenDoNotAddThenGetNothing() {
        assertThat(genreRepository.findAll()).isEqualTo(emptyList());
        assertThat(genreRepository.findByGenreId(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(genreRepository.deleteByGenreId(0)).isFalse();
    }
}
