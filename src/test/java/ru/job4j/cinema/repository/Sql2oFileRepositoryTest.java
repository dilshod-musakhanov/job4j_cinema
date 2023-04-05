package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DatasourceConfiguration;
import ru.job4j.cinema.model.File;


import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Properties;

public class Sql2oFileRepositoryTest {

    private static Sql2oFileRepository fileRepository;
    private static Sql2oFilmRepository filmRepository;
    private static Sql2oFilmSessionRepository filmSessionRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFileRepositoryTest.class.
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

        fileRepository = new Sql2oFileRepository(sql2o);
        filmRepository = new Sql2oFilmRepository(sql2o);
        filmSessionRepository =  new Sql2oFilmSessionRepository(sql2o);

        try (var connection = datasource.getConnection();
             var st = connection.prepareStatement("DELETE FROM film_sessions")) {
            st.executeUpdate();
        }

        try (var connection = datasource.getConnection();
             var st = connection.prepareStatement("DELETE FROM films")) {
            st.executeUpdate();
        }

        try (var connection = datasource.getConnection();
             var st = connection.prepareStatement("DELETE FROM files")) {
            st.executeUpdate();
        }

    }

    @AfterAll
    public static void clearFilesAgain() {
        var fs = filmSessionRepository.findAllFilmSession();
        for (var filmsession : fs) {
            filmSessionRepository.deleteByFilmSessionId(filmsession.getId());
        }
        var films = filmRepository.findAll();
        for (var film : films) {
            filmRepository.deleteByFilmId(film.getId());
        }
        var files = fileRepository.findAll();
        for (var file : files) {
            fileRepository.deleteByFileId(file.getId());
        }
    }

    @AfterEach
    public void clearFiles() {
        var fs = filmSessionRepository.findAllFilmSession();
        for (var filmSession : fs) {
            filmSessionRepository.deleteByFilmSessionId(filmSession.getId());
        }
        var films = filmRepository.findAll();
        for (var film : films) {
            filmRepository.deleteByFilmId(film.getId());
        }
        var files = fileRepository.findAll();
        for (var file : files) {
            fileRepository.deleteByFileId(file.getId());
        }
    }

    @Test
    public void whenAddFileThenGetSame() {
        var file = fileRepository.addFile(new File(
                1,
                "testFile",
                "testFile"
        ));
        var addedFile = fileRepository.findByFileId(file.get().getId());
        assertThat(addedFile).usingRecursiveComparison().isEqualTo(file);
    }

    @Test
    public void whenAddSeveralThenFindAll() {
        var file1 = fileRepository.addFile(new File(
                1,
                "testFile",
                "testFile"
        ));
        var file2 = fileRepository.addFile(new File(
                1,
                "testFile2",
                "testFile2"
        ));
        var files = fileRepository.findAll();
        assertThat(files).usingRecursiveComparison().isEqualTo(List.of(file1.get(), file2.get()));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var file = fileRepository.addFile(new File(
                1,
                "testFile",
                "testFile"
        ));
        var isDeleted = fileRepository.deleteByFileId(file.get().getId());
        var empty = fileRepository.findByFileId(file.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(empty).usingRecursiveComparison().isEqualTo(empty());
    }

}
