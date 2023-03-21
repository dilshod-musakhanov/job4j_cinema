package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionService {

    Optional<FilmSession> addFilmSession(FilmSession filmSession);
    Collection<FilmSession> findAllFilmSession();
    Optional<FilmSession> findByFilmSessionId(int id);
    boolean deleteByFilmSessionId(int id);
    Collection<FilmSessionDto> findAllFilmSessionDto();
}
