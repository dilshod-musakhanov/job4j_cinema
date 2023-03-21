package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> addUser(User user);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findByUserId(int id);

    Collection<User> findAll();

    boolean deleteUserById(int id);
}
