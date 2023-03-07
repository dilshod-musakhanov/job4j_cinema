package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;


@Controller
@RequestMapping("/filmSessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;
    private final FilmService filmService;
    private final HallService hallService;

    public FilmSessionController(FilmSessionService filmSessionService, FilmService filmService, HallService hallService) {
        this.filmSessionService = filmSessionService;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @GetMapping
    public String getAllFilmSessionDto(Model model) {
        model.addAttribute("filmSessions", filmSessionService.findAllFilmSessionDto());
        return "filmSession/filmSessions";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable int id) {
        var optionalFilmSession = filmSessionService.findById(id);
        if (optionalFilmSession.isEmpty()) {
            model.addAttribute("message", "This film session is no longer available");
            return "errors/404";
        }
        var filmSession = optionalFilmSession.get();
        var filmSessionFilm = filmService.getByFilmId(filmSession.getFilmId()).get();
        model.addAttribute("sessionId", filmSession.getId());
        model.addAttribute("rows", hallService.getRowCountByHallId(filmSession.getHallsId()));
        model.addAttribute("places", hallService.getPlacesCountByHallId(filmSession.getHallsId()));
        model.addAttribute("filmName", filmSessionFilm.getName());
        model.addAttribute("filmId", filmSessionFilm.getId());
        return "ticket/buyTicket";
    }
}
