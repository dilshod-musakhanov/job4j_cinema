package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DatasourceConfiguration;
import ru.job4j.cinema.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class  Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository ticketRepository;
    private static Sql2oFilmSessionRepository filmSessionRepository;
    private static Sql2oFilmRepository filmRepository;
    private static Sql2oFileRepository fileRepository;
    private static Sql2oGenreRepository genreRepository;
    private static Sql2oHallRepository hallRepository;
    private static Sql2oUserRepository userRepository;
    private static File file;
    private static Genre genre;
    private static Film film;
    private static Hall hall;
    private static FilmSession filmSession;
    private static User user;
    private static LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private static LocalDateTime endTime = startTime.plusMinutes(100).truncatedTo(ChronoUnit.MINUTES);

    @BeforeAll
    public static void initRepositories() throws IOException {
        var properties = new Properties();
        try (var inStream = Sql2oTicketRepositoryTest.class
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

        ticketRepository = new Sql2oTicketRepository(sql2o);
        filmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        filmRepository = new Sql2oFilmRepository(sql2o);
        fileRepository = new Sql2oFileRepository(sql2o);
        genreRepository = new Sql2oGenreRepository(sql2o);
        hallRepository = new Sql2oHallRepository(sql2o);
        userRepository = new Sql2oUserRepository(sql2o);

        file = new File(
                5,
                "testFile",
                "testFile"
        );
        fileRepository.addFile(file);

        genre = new Genre(
                1,
                "Mix"
        );
        genreRepository.addGenre(genre);

        film = new Film(
                1,
                "Avatar",
                "Good movie",
                1, genre.getId(),
                16,
                120,
                file.getId()
        );
        filmRepository.addFilm(film);

        hall = new Hall(
                1,
                "VIP",
                10,
                10,
                "private hall"
        );
        hallRepository.addHall(hall);

        filmSession = new FilmSession(
                1,
                film.getId(),
                hall.getId(),
                startTime,
                endTime
        );
        filmSessionRepository.addFilmSession(filmSession);

        user = new User(
                1,
                "Bob",
                "bob@b.com",
                "bobik"
        );
        userRepository.addUser(user);
    }

    @AfterAll
    public static void deleteReps() {
        filmSessionRepository.deleteByFilmSessionId(filmSession.getId());
        filmRepository.deleteByFilmId(film.getId());
        fileRepository.deleteByFileId(file.getId());
        genreRepository.deleteByGenreId(genre.getId());
        hallRepository.deleteByHallId(hall.getId());
        userRepository.deleteUserById(user.getId());
    }

    @AfterEach
    public void clearTickets() {
        var tickets = ticketRepository.findAll();
        for (var ticket : tickets) {
            ticketRepository.deleteTicketById(ticket.getId());
        }
    }

    @Test
    public void whenAddTicketGetSameTicket() {
        var ticket = ticketRepository.addTicket(new Ticket(
                1,
                filmSession.getId(),
                1,
                1,
                user.getId())
        );
        var addedTicket = ticketRepository.findByTicketId(ticket.get().getId());
        assertThat(addedTicket).usingRecursiveComparison().isEqualTo(ticket);
    }

    @Test
    public void whenAddSeveralThenFindAll() {
        var ticket1 = ticketRepository.addTicket(new Ticket(
                1,
                filmSession.getId(),
                10,
                1,
                user.getId())
        );
        var ticket2 = ticketRepository.addTicket(new Ticket(
                2,
                filmSession.getId(),
                20,
                2,
                user.getId())
        );
        var tickets = ticketRepository.findAll();
        assertThat(tickets).isEqualTo(List.of(ticket1.get(), ticket2.get()));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var ticket = ticketRepository.addTicket(new Ticket(
                1,
                filmSession.getId(),
                7,
                7,
                user.getId())
        );
        var isDeleted = ticketRepository.deleteTicketById(ticket.get().getId());
        var addedTicket = ticketRepository.findByTicketId(ticket.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(addedTicket).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(ticketRepository.deleteTicketById(0)).isFalse();
    }

    @Test
    public void whenDoNotAddThenGetEmptyList() {
        assertThat(ticketRepository.findAll()).isEqualTo(emptyList());
    }
}
