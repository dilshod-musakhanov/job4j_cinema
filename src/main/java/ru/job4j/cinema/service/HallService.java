package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.Optional;

public interface HallService {

    Optional<Hall> addHall(Hall hall);
    Collection<Hall> findAll();
    Optional<Hall> findByHallId(int id);
    boolean deleteByHallId(int id);
    Collection<Integer> getRowCountByHallId(int hallId);
    Collection<Integer> getPlacesCountByHallId(int hallId);
}
