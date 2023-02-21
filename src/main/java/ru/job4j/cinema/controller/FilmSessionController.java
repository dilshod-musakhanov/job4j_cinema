package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.FilmSessionService;

@Controller
public class FilmSessionController {

    FilmSessionService filmSessionService;

    public FilmSessionController(FilmSessionService filmSessionService) {
        this.filmSessionService = filmSessionService;
    }

    @GetMapping("/filmSessions")
    public String getAllFilmSessionDto(Model model) {
        model.addAttribute("filmSessions", filmSessionService.findAllFilmSessionDto());
        return "filmSessions";
    }
}
