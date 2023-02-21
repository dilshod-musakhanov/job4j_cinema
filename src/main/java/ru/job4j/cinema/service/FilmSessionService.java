package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;

public interface FilmSessionService {

    Collection<FilmSession> findAllFilmSession();
    Collection<FilmSessionDto> findAllFilmSessionDto();
}
