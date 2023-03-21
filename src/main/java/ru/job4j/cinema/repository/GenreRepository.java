package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {

    Optional<Genre> addGenre(Genre genre);
    Optional<Genre> findByGenreId(int id);
    Collection<Genre> findAll();
    boolean deleteByGenreId(int id);
}
