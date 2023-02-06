package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.HallService;

import java.util.Collection;

@Controller
public class IndexController {

    private final HallService hallService;

    public IndexController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping("/index")
    public String mainPageWithHalls(Model model) {
        model.addAttribute("halls", hallService.findAll());
        return "index";
    }
}
