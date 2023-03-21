package ru.job4j.cinema.service;


import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleHallService implements HallService {

    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository sql2oHallRepository) {
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<Hall> addHall(Hall hall) {
        return hallRepository.addHall(hall);
    }

    @Override
    public Collection<Hall> findAll() {
        return hallRepository.findAll();
    }

    @Override
    public Optional<Hall> findByHallId(int id) {
        return hallRepository.findByHallId(id);
    }

    @Override
    public boolean deleteByHallId(int id) {
        return hallRepository.deleteByHallId(id);
    }

    @Override
    public Collection<Integer> getRowCountByHallId(int hallId) {
        return hallRepository.getRowCountByHallId(hallId);
    }

    @Override
    public Collection<Integer> getPlacesCountByHallId(int hallId) {
        return hallRepository.getPlacesCountByHallId(hallId);
    }
}
