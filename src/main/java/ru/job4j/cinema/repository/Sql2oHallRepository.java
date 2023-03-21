package ru.job4j.cinema.repository;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oHallRepository implements HallRepository {

    private static final Logger LOG = Logger.getLogger(Sql2oTicketRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Hall> addHall(Hall hall) {
        Optional<Hall> optionalHall = Optional.empty();
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO halls (name, row_count, place_count, description)
                    VALUES (:name, :rowCount, :placeCount, :description)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("name", hall.getName())
                    .addParameter("rowCount", hall.getRowCount())
                    .addParameter("placeCount", hall.getPlaceCount())
                    .addParameter("description", hall.getDescription());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            hall.setId(generatedId);
            optionalHall = Optional.of(hall);
        } catch (Exception e) {
            LOG.error("Exception in adding Hall " + hall.getName(), e);
        }
        return optionalHall;
    }

    @Override
    public Collection<Hall> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM halls")
                    .setColumnMappings(Hall.COLUMN_MAPPING)
                    .executeAndFetch(Hall.class);
            return query;
        }
    }

    @Override
    public Optional<Hall> findByHallId(int id) {
        Optional<Hall> optionalHall;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM halls WHERE id = :id")
                    .addParameter("id", id);
            var hall = query.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetchFirst(Hall.class);
            optionalHall = Optional.ofNullable(hall);
            if (optionalHall.isEmpty()) {
                LOG.error("Exception in finding Hall by id :" + id);
            }
            return Optional.ofNullable(hall);
        }
    }

    @Override
    public boolean deleteByHallId(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM halls WHERE id = :id")
                    .addParameter("id", id).executeUpdate().getResult();
            connection.commit();
            return query > 0;
        }
    }

    @Override
    public Collection<Integer> getRowCountByHallId(int hallId) {
        Collection<Integer> rows = new ArrayList<>();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM halls WHERE id = :id")
                    .addParameter("id", hallId)
                    .setColumnMappings(Hall.COLUMN_MAPPING)
                    .executeAndFetchFirst(Hall.class);
            int total = query.getRowCount();
            for (int i = 1; i <= total; i++) {
                rows.add(i);
            }
        }
        return rows;
    }

    @Override
    public Collection<Integer> getPlacesCountByHallId(int hallId) {
        Collection<Integer> places = new ArrayList<>();
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM halls WHERE id = :id")
                    .addParameter("id", hallId)
                    .setColumnMappings(Hall.COLUMN_MAPPING)
                    .executeAndFetchFirst(Hall.class);
            int total = query.getPlaceCount();
            for (int i = 1; i <= total; i++) {
                places.add(i);
            }
        }
        return places;
    }

}
