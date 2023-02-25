package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.GenreService;
import ru.job4j.cinema.util.HttpSessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public String genres(Model model, HttpSession session) {
        HttpSessionUtil.passUserAttribute(model, session);
        model.addAttribute("genres", genreService.findAll());
        return "genres";
    }
}
