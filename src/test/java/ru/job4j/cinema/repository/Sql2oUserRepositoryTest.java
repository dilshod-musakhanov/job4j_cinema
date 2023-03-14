package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.
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

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteUserById(user.getId());
        }
    }

    @Test
    public void whenAddThenGetSame() {
        var user = sql2oUserRepository.add(new User(
                1,
                "Ali Baba",
                "alibaba@alibaba.com",
                "alibaba"
        ));
        var addedUser = sql2oUserRepository.findById(user.get().getId());
        assertThat(user).usingRecursiveComparison().isEqualTo(addedUser);
    }

    @Test
    public void whenAddSameUserTwiceThenGetEmpty() {
        User user = new User(
                1,
                "Ali Baba",
                "alibaba@alibaba.com",
                "alibaba"
        );
        var addedUser = sql2oUserRepository.add(user);
        var addedSameUser = sql2oUserRepository.add(user);
        assertThat(addedUser.get()).usingRecursiveComparison().isEqualTo(user);
        assertThat(addedSameUser).isEmpty();
    }

    @Test
    public void whenFindUserByEmailAndPassword() {
        var user = sql2oUserRepository.add(new User(
                1,
                "Ali Baba",
                "alibaba@alibaba.com",
                "alibaba"
        ));
        var addedUser = sql2oUserRepository.findUserByEmailAndPassword(
                user.get().getEmail(),
                user.get().getPassword()
        );
        assertThat(user).usingRecursiveComparison().isEqualTo(addedUser);
    }

    @Test
    public void whenFindAll() {
        User user1 = new User(
                1,
                "Ali Baba",
                "alibaba@alibaba.com",
                "alibaba"
        );
        User user2 = new User(
                2,
                "John Doe",
                "john@doe.com",
                "jdoe"
        );
        sql2oUserRepository.add(user1);
        sql2oUserRepository.add(user2);
        var users = sql2oUserRepository.findAll();
        assertThat(users).isEqualTo(List.of(user1, user2));
    }

    @Test
    public void whenDeleteUserById() {
        User user = new User(
                1,
                "Ali Baba",
                "alibaba@alibaba.com",
                "alibaba"
        );
        sql2oUserRepository.add(user);
        var isDeleted = sql2oUserRepository.deleteUserById(user.getId());
        var deletedUser = sql2oUserRepository.findById(user.getId());
        assertThat(isDeleted).isTrue();
        assertThat(deletedUser).isEmpty();
    }
}
