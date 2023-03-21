package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleGenreService implements GenreService {

    private final GenreRepository genreRepository;

    public SimpleGenreService(GenreRepository sql2oGenreRepository) {
        this.genreRepository = sql2oGenreRepository;
    }

    @Override
    public Optional<Genre> addGenre(Genre genre) {
        return genreRepository.addGenre(genre);
    }

    @Override
    public Optional<Genre> findByGenreId(int id) {
        return genreRepository.findByGenreId(id);
    }

    @Override
    public Collection<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public boolean deleteByGenreId(int id) {
        return genreRepository.deleteByGenreId(id);
    }
}
