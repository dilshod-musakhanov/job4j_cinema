package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.util.HttpSessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public String getAllFilmDto(Model model, HttpSession session) {
        HttpSessionUtil.passUserAttribute(model, session);
        model.addAttribute("films", filmService.findAllFilmDto());
        return "films";
    }

}
