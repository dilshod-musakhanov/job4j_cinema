package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oHallRepositoryTest {

    private static Sql2oHallRepository hallRepository;
    private static Sql2oFilmSessionRepository filmSessionRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
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

        hallRepository = new Sql2oHallRepository(sql2o);
        filmSessionRepository = new Sql2oFilmSessionRepository(sql2o);

        try (var connection = datasource.getConnection();
            var st = connection.prepareStatement("DELETE FROM halls")) {
            st.executeUpdate();
        }
    }

    @AfterAll
    public static void deleteRep() {
        var filmSessions = filmSessionRepository.findAllFilmSession();
        for (var filmSession : filmSessions) {
            filmSessionRepository.deleteByFilmSessionId(filmSession.getId());
        }
        var halls = hallRepository.findAll();
        for (var hall : halls) {
            hallRepository.deleteByHallId(hall.getId());
        }
    }

    @AfterEach
    public void clearHalls() {
        var halls = hallRepository.findAll();
        for (var hall : halls) {
            hallRepository.deleteByHallId(hall.getId());
        }
    }


    @Test
    public void whenAddThenGetSame() {
        var hall = hallRepository.addHall(
                new Hall(
                        1,
                        "HOME",
                        6,
                        5,
                        "Cosy hall"
                )
        );
        var addedHall = hallRepository.findByHallId(hall.get().getId());
        assertThat(addedHall).usingRecursiveComparison().isEqualTo(hall);
    }

    @Test
    public void whenAddSeveralThenFindAll() {
        Hall hall1 = new Hall(
                1,
                "HOME",
                6,
                5,
                "Cosy hall"
        );
        Hall hall2 = new Hall(
                2,
                "GROUND",
                6,
                5,
                "Very Cosy hall"
        );
        hallRepository.addHall(hall1);
        hallRepository.addHall(hall2);
        var halls = hallRepository.findAll();
        assertThat(halls).usingRecursiveComparison().isEqualTo(List.of(hall1, hall2));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var hall = hallRepository.addHall(
                new Hall(
                        1,
                        "HOME",
                        6,
                        5,
                        "Cosy hall"
                )
        );
        hallRepository.findByHallId(hall.get().getId());
        var isDeleted = hallRepository.deleteByHallId(hall.get().getId());
        var empty = hallRepository.findByHallId(hall.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(empty).isEmpty();
    }

    @Test
    public void whenDeleteWithInvalidIdThenGetFalse() {
        var isDeleted = hallRepository.deleteByHallId(0);
        assertThat(isDeleted).isFalse();
    }

    @Test
    public void whenAddNothingThenGetEmptyList() {
        var all = hallRepository.findAll();
        assertThat(all).usingRecursiveComparison().isEqualTo(emptyList());
    }

}
