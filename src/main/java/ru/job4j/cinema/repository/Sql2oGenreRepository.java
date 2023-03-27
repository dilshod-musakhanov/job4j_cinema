package ru.job4j.cinema.repository;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oGenreRepository implements GenreRepository {

    private static final Logger LOG = Logger.getLogger(Sql2oUserRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Genre> addGenre(Genre genre) {
        Optional<Genre> optionalGenre = Optional.empty();
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO genres (name)
                    VALUES (:name)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("name", genre.getName());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            genre.setId(generatedId);
            optionalGenre = Optional.of(genre);
        } catch (Exception e) {
            LOG.error("Exception in adding Genre " + genre.getName(), e);
        }
        return optionalGenre;
    }

    @Override
    public Optional<Genre> findByGenreId(int id) {
        Optional<Genre> optionalGenre = Optional.empty();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM genres WHERE id = :id");
            query.addParameter("id", id);
            var genre = query.executeAndFetchFirst(Genre.class);
            optionalGenre = Optional.ofNullable(genre);
            return optionalGenre;
        } catch (Exception e) {
            LOG.error("Exception in finding Genre by id: " + id + " : " + e);
        }
        return optionalGenre;
    }

    @Override
    public Collection<Genre> findAll() {
        Collection<Genre> genres = new ArrayList<>();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM genres");
            genres = query.executeAndFetch(Genre.class);
        } catch (Exception e) {
            LOG.error("Exception in finding all Genre: " + e);
        }
        return genres;
    }

    @Override
    public boolean deleteByGenreId(int id) {
        boolean result = false;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM genres WHERE id = :id");
            query.addParameter("id", id);
            var affectedRows = query.executeUpdate().getResult();
            result = affectedRows > 0;
        } catch (Exception e) {
            LOG.error("Exception in deleting Genre by id: " + id + " : " + e);
        }
        return result;
    }
}
