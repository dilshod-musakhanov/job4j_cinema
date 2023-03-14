package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oUserRepository implements UserRepository {

    private static final Logger LOG = Logger.getLogger(Sql2oUserRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> add(User user) {
        Optional<User> optionalUser = Optional.empty();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                            """
                    INSERT INTO users (full_name, email, password)
                    VALUES (:full_name, :email, :password)""", true
                    )
                    .addParameter("full_name", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            optionalUser = Optional.of(user);
            LOG.error("not error just to see what is added " + user.getFullName() + " " + user.getId());
        } catch (Exception e) {
            LOG.error("Exception in adding User by email :" + user.getEmail() + " " + e);
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> optionalUser;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                    "SELECT * FROM users WHERE email = :email AND password = :password"
                    )
                    .addParameter("email", email)
                    .addParameter("password", password);
            var user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            optionalUser = Optional.ofNullable(user);
            if (optionalUser.isEmpty()) {
                LOG.error("Exception in finding User by email :" + email);
            }
            return optionalUser;
        }
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> optionalUser;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users WHERE id = :id")
                    .addParameter("id", id);
            var user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            optionalUser = Optional.ofNullable(user);
            if (optionalUser.isEmpty()) {
                LOG.error("Exception in finding User by id :" + id);
            }
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            return query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetch(User.class);
        }
    }

    @Override
    public boolean deleteUserById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE id = :id")
                    .addParameter("id", id);
            var result = query.executeUpdate().getResult();
            LOG.error("just to see the userId  **** " + id);
            var result2 = connection.createQuery("truncate table users").executeUpdate().getResult();
            LOG.error("just to see result  **** " + result);
            LOG.error("just to see result2  **** " + result2);
            return result > 0;
        }
    }
}
