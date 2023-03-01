package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public class Sql2oHallRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
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
