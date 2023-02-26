package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.util.HttpSessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    private final HallService hallService;

    public IndexController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping({"/", "/index"})
    public String mainPageWithHalls(Model model, HttpSession session) {
        HttpSessionUtil.passUserAttribute(model, session);
        model.addAttribute("halls", hallService.findAll());
        return "index";
    }
}
