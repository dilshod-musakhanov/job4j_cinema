package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmService {

    Optional<Film> addFilm(Film film);
    Collection<Film> findAll();
    Optional<Film> findByFilmId(int filmId);
    boolean deleteByFilmId(int id);
    Collection<FilmDto> findAllFilmDto();
}
