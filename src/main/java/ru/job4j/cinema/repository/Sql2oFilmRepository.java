package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.service.GenreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class Sql2oFilmRepository implements FilmRepository {

    private final Sql2o sql2o;

    private final FilmDto filmDto;

    private final GenreService genreService;


    public Sql2oFilmRepository(Sql2o sql2o, FilmDto filmDto, GenreService genreService) {
        this.sql2o = sql2o;
        this.filmDto = filmDto;
        this.genreService = genreService;
    }

    @Override
    public Collection<Film> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM films");
            return query.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetch(Film.class);
        }

    }

    @Override
    public Collection<FilmDto> getAllFilmDto() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM films");
            Collection<Film> films = query.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetch(Film.class);
            Collection<Genre> genres = genreService.findAll();
            List<FilmDto> filmDtos = new ArrayList<>();
            for (Film f : films) {
                for (Genre g : genres) {
                    if (g.getId() == f.getGenreId()) {
                        FilmDto filmDto = new FilmDto();
                        filmDto.setId(f.getId());
                        filmDto.setName(f.getName());
                        filmDto.setDescription(f.getDescription());
                        filmDto.setYear(f.getYear());
                        filmDto.setGenre(new Genre(g.getId(), g.getName()));
                        filmDto.setMinimalAge(f.getMinimalAge());
                        filmDto.setDurationInMinutes(f.getDurationInMinutes());
                        filmDto.setFileId(f.getFileId());
                        filmDtos.add(filmDto);
                    }
                }
            }
            return filmDtos;
        }
    }
}
