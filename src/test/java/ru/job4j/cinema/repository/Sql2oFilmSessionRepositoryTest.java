package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DatasourceConfiguration;
import ru.job4j.cinema.model.*;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

public class Sql2oFilmSessionRepositoryTest {

    private static Sql2oFilmSessionRepository filmSessionRepository;
    private static Sql2oFilmRepository filmRepository;
    private static Sql2oFileRepository fileRepository;
    private static Sql2oGenreRepository genreRepository;
    private static Sql2oHallRepository hallRepository;
    private static File file;
    private static Genre genre;
    private static Film film;
    private static Hall hall;
    private static LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private static LocalDateTime endTime = startTime.plusMinutes(100).truncatedTo(ChronoUnit.MINUTES);

    @BeforeAll
    public static void initRepositories() throws IOException {
        var properties = new Properties();
        try (var inStream = Sql2oFilmSessionRepositoryTest.class
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

        filmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        filmRepository = new Sql2oFilmRepository(sql2o);
        fileRepository = new Sql2oFileRepository(sql2o);
        genreRepository = new Sql2oGenreRepository(sql2o);
        hallRepository = new Sql2oHallRepository(sql2o);

        file = new File(
                1,
                "testFile",
                "testFile"
        );
        fileRepository.addFile(file);

        genre = new Genre(
                1,
                "Adventure"
        );
        genreRepository.addGenre(genre);

        film = new Film(
                1,
                "Forest Gump",
                "Good movie",
                1987,
                genre.getId(),
                18,
                120,
                file.getId()
        );
        filmRepository.addFilm(film);

        hall = new Hall(1,
                "VIP",
                10,
                10,
                "private"
        );
        hallRepository.addHall(hall);

        var filmSessions = filmSessionRepository.findAllFilmSession();
        for (var filmSession : filmSessions) {
            filmSessionRepository.deleteByFilmSessionId(filmSession.getId());
        }
    }

    @AfterAll
    public static void deleteFile() {
        filmRepository.deleteByFilmId(film.getId());
        fileRepository.deleteByFileId(file.getId());
        genreRepository.deleteByGenreId(genre.getId());
        hallRepository.deleteByHallId(hall.getId());
    }

    @AfterEach
    public void clearFilmSession() {
        var filmSessions = filmSessionRepository.findAllFilmSession();
        for (var filmSession : filmSessions) {
            filmSessionRepository.deleteByFilmSessionId(filmSession.getId());
        }
    }

    @Test
    public void whenAddFilmSessionThenGetSame() {
        var filmSession = filmSessionRepository.addFilmSession(new FilmSession(
                1,
                film.getId(),
                hall.getId(),
                startTime,
                endTime
        ));
        var addedFilmSession = filmSessionRepository.findByFilmSessionId(filmSession.get().getId());
        assertThat(addedFilmSession).usingRecursiveComparison().isEqualTo(filmSession);
    }

    @Test
    public void whenAddSeveralThenFindAll() {
        var filmSession1 = filmSessionRepository.addFilmSession(new FilmSession(
                1,
                film.getId(),
                hall.getId(),
                startTime,
                endTime)
        );
        var filmSession2 = filmSessionRepository.addFilmSession(new FilmSession(
                2,
                film.getId(),
                hall.getId(),
                startTime,
                endTime)
        );
        var filmSessions = filmSessionRepository.findAllFilmSession();
        assertThat(filmSessions).isEqualTo(List.of(filmSession1.get(), filmSession2.get()));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var filmSession = filmSessionRepository.addFilmSession(new FilmSession(
                1,
                film.getId(),
                hall.getId(),
                startTime,
                endTime)
        );
        var isDeleted = filmSessionRepository.deleteByFilmSessionId(filmSession.get().getId());
        var savedFilmSession = filmSessionRepository.findByFilmSessionId(filmSession.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedFilmSession).isEqualTo(empty());
    }

    @Test
    public void whenDoNotAddThenGetNothing() {
        assertThat(filmSessionRepository.findAllFilmSession()).isEqualTo(emptyList());
        assertThat(filmSessionRepository.findByFilmSessionId(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(filmSessionRepository.deleteByFilmSessionId(0)).isFalse();
    }
}
