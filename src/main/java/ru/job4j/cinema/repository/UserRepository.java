package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> addUser(User user);
    Optional<User> findUserByEmailAndPassword(String email, String password);
    Optional<User> findByUserId(int id);
    Collection<User> findAll();
    boolean deleteUserById(int id);

}
