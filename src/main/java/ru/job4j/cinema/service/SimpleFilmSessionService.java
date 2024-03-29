package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public SimpleFilmSessionService(FilmSessionRepository sql2oFilmSessionRepository, FilmRepository sql2oFilmRepository,
                                    HallRepository sql2oHallRepository) {
        this.filmSessionRepository = sql2oFilmSessionRepository;
        this.filmRepository = sql2oFilmRepository;
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<FilmSession> addFilmSession(FilmSession filmSession) {
        return filmSessionRepository.addFilmSession(filmSession);
    }

    @Override
    public Collection<FilmSession> findAllFilmSession() {
        return filmSessionRepository.findAllFilmSession();
    }

    @Override
    public Optional<FilmSession> findByFilmSessionId(int id) {
        return filmSessionRepository.findByFilmSessionId(id);
    }

    @Override
    public boolean deleteByFilmSessionId(int id) {
        return filmSessionRepository.deleteByFilmSessionId(id);
    }

    @Override
    public Collection<FilmSessionDto> findAllFilmSessionDto() {
        var filmSessions = findAllFilmSession();
        var films = filmRepository.findAll().stream().collect(toMap(Film::getId, Film::getName));
        var halls = hallRepository.findAll().stream().collect(toMap(Hall::getId, Hall::getName));
        var filmSessionDtos = new ArrayList<FilmSessionDto>();
        for (var filmSession : filmSessions) {
            var filmSessionDto = createDto(filmSession, films, halls);
            filmSessionDtos.add(filmSessionDto);
        }
        return filmSessionDtos;
    }

    private FilmSessionDto createDto(FilmSession filmSession, Map<Integer, String> films, Map<Integer, String> halls) {
        var filmSessionDto = new FilmSessionDto();
        filmSessionDto.setId(filmSession.getId());
        filmSessionDto.setFilm(films.get(filmSession.getFilmId()));
        filmSessionDto.setHall(halls.get(filmSession.getHallsId()));
        filmSessionDto.setStartTime(filmSession.getStartTime());
        filmSessionDto.setEndTime(filmSession.getEndTime());
        return filmSessionDto;
    }

}
