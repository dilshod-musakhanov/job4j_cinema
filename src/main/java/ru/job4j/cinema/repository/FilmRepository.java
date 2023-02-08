package ru.job4j.cinema.repository;

import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;

import java.util.Collection;

public interface FilmRepository {

    Collection<Film> findAll();

    Collection<FilmDto> getAllFilmDto();
}
