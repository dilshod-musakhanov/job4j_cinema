package ru.job4j.cinema.repository;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oFilmRepository implements FilmRepository {

    private static final Logger LOG = Logger.getLogger(Sql2oTicketRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        Optional<Film> optionalFilm = Optional.empty();
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO films(name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                    VALUES (:name, :description, :year, :genreId, :minimalAge, :durationInMinutes, :fileId)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("name", film.getName())
                    .addParameter("description", film.getDescription())
                    .addParameter("year", film.getYear())
                    .addParameter("genreId", film.getGenreId())
                    .addParameter("minimalAge", film.getMinimalAge())
                    .addParameter("durationInMinutes", film.getDurationInMinutes())
                    .addParameter("fileId", film.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            film.setId(generatedId);
            optionalFilm = Optional.of(film);
        } catch (Exception e) {
            LOG.error("Exception in adding Film: " + film + " : " + e);
        }
        return optionalFilm;
    }

    @Override
    public boolean deleteByFilmId(int id) {
        boolean result = false;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM films WHERE id = :id");
            query.addParameter("id", id);
            var affectedRows = query.executeUpdate().getResult();
            result = affectedRows > 0;
        } catch (Exception e) {
            LOG.error("Exception in deleting Film by id: " + id + " : " + e);
        }
        return result;
    }

    @Override
    public Collection<Film> findAll() {
        Collection<Film> films = new ArrayList<>();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM films");
            films = query.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetch(Film.class);
        } catch (Exception e) {
            LOG.error("Exception in finding all Films: " + e);
        }
        return films;
    }

    @Override
    public Optional<Film> findByFilmId(int id) {
        Film film = new Film();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM films WHERE id = :id")
                    .addParameter("id", id);
            film = query.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetchFirst(Film.class);
        } catch (Exception e) {
            LOG.error("Exception in finding Film by id: " + id + " : " + e);
        }
        return Optional.ofNullable(film);
    }

}
