package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;
import org.apache.log4j.Logger;

import java.util.ArrayList;
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
    public Optional<User> addUser(User user) {
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
        } catch (Exception e) {
            LOG.error("Exception in adding User by email :" + user.getEmail() + " " + e);
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> optionalUser = Optional.empty();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                    "SELECT * FROM users WHERE email = :email AND password = :password"
                    )
                    .addParameter("email", email)
                    .addParameter("password", password);
            var user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            optionalUser = Optional.ofNullable(user);
        } catch (Exception e) {
            LOG.error("Exception in finding User by email :" + email + " : " + e);
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findByUserId(int id) {
        Optional<User> optionalUser = Optional.empty();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users WHERE id = :id")
                    .addParameter("id", id);
            var user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            optionalUser = Optional.ofNullable(user);
        } catch (Exception e) {
            LOG.error("Exception in finding User by id :" + id + " : " + e);
        }
        return optionalUser;
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> users = new ArrayList<>();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            users =  query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetch(User.class);
        } catch (Exception e) {
            LOG.error("Exception in finding all User " + e);
        }
        return users;
    }

    @Override
    public boolean deleteUserById(int id) {
        boolean result = false;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE id = :id")
                    .addParameter("id", id).executeUpdate().getResult();
            connection.createQuery("TRUNCATE TABLE users RESTART IDENTITY").executeUpdate();
            connection.commit();
            result =  query > 0;
        } catch (Exception e) {
            LOG.error("Exception in deleting User by id: " + id + " : " + e);
        }
        return result;
    }
}
