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

    private static Sql2oUserRepository userRepository;

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

        userRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = userRepository.findAll();
        for (var user : users) {
            userRepository.deleteUserById(user.getId());
        }
    }

    @Test
    public void whenAddThenGetSame() {
        var user = userRepository.addUser(new User(
                1,
                "Vali Baba",
                "valibaba@alibaba.com",
                "alibaba"
        ));
        var addedUser = userRepository.findByUserId(user.get().getId());
        assertThat(user).usingRecursiveComparison().isEqualTo(addedUser);
    }

    @Test
    public void whenAddSameUserTwiceThenGetEmpty() {
        User user = new User(
                1,
                "Dali Baba",
                "dalibaba@alibaba.com",
                "alibaba"
        );
        var addedUser = userRepository.addUser(user);
        var addedSameUser = userRepository.addUser(user);
        assertThat(addedUser.get()).usingRecursiveComparison().isEqualTo(user);
        assertThat(addedSameUser).isEmpty();
    }

    @Test
    public void whenFindUserByEmailAndPassword() {
        var user = userRepository.addUser(new User(
                1,
                "Ali Baba",
                "alibaba@alibaba.com",
                "alibaba"
        ));
        var addedUser = userRepository.findUserByEmailAndPassword(
                user.get().getEmail(),
                user.get().getPassword()
        );
        assertThat(user).usingRecursiveComparison().isEqualTo(addedUser);
    }

    @Test
    public void whenAddSeveralThenFindAll() {
        User user1 = new User(
                1,
                "Gali Baba",
                "galibaba@alibaba.com",
                "alibaba"
        );
        User user2 = new User(
                2,
                "John Doe",
                "john@doe.com",
                "jdoe"
        );
        userRepository.addUser(user1);
        userRepository.addUser(user2);
        var users = userRepository.findAll();
        assertThat(users).isEqualTo(List.of(user1, user2));
    }

    @Test
    public void whenDeleteUserById() {
        User user = new User(
                1,
                "Zali Baba",
                "zalibaba@alibaba.com",
                "alibaba"
        );
        userRepository.addUser(user);
        var isDeleted = userRepository.deleteUserById(user.getId());
        var deletedUser = userRepository.findByUserId(user.getId());
        assertThat(isDeleted).isTrue();
        assertThat(deletedUser).isEmpty();
    }
}
