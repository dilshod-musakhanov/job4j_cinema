package ru.job4j.cinema.repository;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {

    private static final Logger LOG = Logger.getLogger(Sql2oTicketRepository.class.getName());
    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<File> addFile(File file) {
        Optional<File> optionalFile = Optional.empty();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("INSERT INTO files (name, path) VALUES (:name, :path)", true)
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            file.setId(generatedId);
            optionalFile = Optional.of(file);
        } catch (Exception e) {
            LOG.error("Exception in adding File " + file + " " + e);
        }
        return optionalFile;
    }

    @Override
    public Optional<File> findByFileId(int id) {
        Optional<File> optionalFile = Optional.empty();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM files WHERE id  = :id");
            var file = query.addParameter("id", id).executeAndFetchFirst(File.class);
            optionalFile = Optional.ofNullable(file);
            if (optionalFile.isEmpty()) {
                LOG.error("Exception in finding File by id " + id);
            }
            return optionalFile;
        }
    }

    @Override
    public Collection<File> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM files");
            return query.executeAndFetch(File.class);
        }
    }

    @Override
    public boolean deleteByFileId(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM files WHERE id = :id");
            var affectedRows = query.addParameter("id", id).executeUpdate().getResult();
            return affectedRows > 0;
        }
    }
}
