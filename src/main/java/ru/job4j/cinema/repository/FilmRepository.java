package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {

    Optional<Film> addFilm(Film film);
    Collection<Film> findAll();
    Optional<Film> findByFilmId(int filmId);
    boolean deleteByFilmId(int id);
}
