package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionRepository {

    Optional<FilmSession> addFilmSession(FilmSession filmSession);
    Collection<FilmSession> findAllFilmSession();
    Optional<FilmSession> findByFilmSessionId(int id);
    boolean deleteByFilmSessionId(int id);
}
