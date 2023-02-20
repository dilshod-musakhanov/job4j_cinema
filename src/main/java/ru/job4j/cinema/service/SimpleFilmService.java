package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;

    public SimpleFilmService(FilmRepository sql2oFilmRepository, GenreRepository genreRepository) {
        this.filmRepository = sql2oFilmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Collection<FilmDto> findAllFilmDto() {
        var films = findAll();
        var genres = genreRepository.findAll().stream().collect(toMap(Genre::getId, g -> g));
        var filmDtos = new ArrayList<FilmDto>();
        for (var film : films) {
            var filmDto = createDto(genres, film);
            filmDtos.add(filmDto);
        }
        return filmDtos;
    }

    private FilmDto createDto(Map<Integer, Genre> genres, Film film) {
        var filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setYear(film.getYear());
        filmDto.setGenre(genres.get(film.getGenreId()));
        filmDto.setMinimalAge(film.getMinimalAge());
        filmDto.setDurationInMinutes(film.getDurationInMinutes());
        filmDto.setFileId(film.getFileId());
        return filmDto;
    }
}
