package ru.job4j.cinema.repository;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFilmSessionRepository implements FilmSessionRepository {

    private static final Logger LOG = Logger.getLogger(FilmSessionRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oFilmSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<FilmSession> addFilmSession(FilmSession filmSession) {
        Optional<FilmSession> optionalFilmSession = Optional.empty();
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO film_sessions (film_id, halls_id, start_time, end_time)
                    VALUES (:filmId, :hallsId, :startTime, :endTime)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("filmId", filmSession.getFilmId())
                    .addParameter("hallsId", filmSession.getHallsId())
                    .addParameter("startTime", filmSession.getStartTime())
                    .addParameter("endTime", filmSession.getEndTime());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            filmSession.setId(generatedId);
            optionalFilmSession = Optional.of(filmSession);
        } catch (Exception e) {
            LOG.error("Exception in adding FilmSession " + filmSession + " " + e);
        }
        return optionalFilmSession;
    }

    @Override
    public Collection<FilmSession> findAllFilmSession() {
        Collection<FilmSession> filmSessions = new ArrayList<>();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM film_sessions");
            filmSessions = query.setColumnMappings(FilmSession.COLUMN_MAPPING)
                    .executeAndFetch(FilmSession.class);
        } catch (Exception e) {
            LOG.error("Exception in finding all FilmSession: " + e);
        }
        return filmSessions;
    }

    @Override
    public Optional<FilmSession> findByFilmSessionId(int id) {
        Optional<FilmSession> optionalFilmSession = Optional.empty();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM film_sessions WHERE id = :id");
            query.addParameter("id", id);
            var filmSession = query.setColumnMappings(FilmSession.COLUMN_MAPPING).executeAndFetchFirst(FilmSession.class);
            optionalFilmSession = Optional.ofNullable(filmSession);
        } catch (Exception e) {
            LOG.error("Exception in finding FilmSession by id: " + id + " : " + e);
        }
        return optionalFilmSession;
    }

    @Override
    public boolean deleteByFilmSessionId(int id) {
        boolean result = false;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM film_sessions WHERE id = :id");
            query.addParameter("id", id);
            var affectedRows = query.executeUpdate().getResult();
            result = affectedRows > 0;
        } catch (Exception e) {
            LOG.error("Exception in deleting FilmSession by id: " + id + " : " + e);
        }
        return result;
    }
}
