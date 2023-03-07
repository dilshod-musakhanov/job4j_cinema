package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/formAddUser")
    public String addUser() {
        return "user/addUser";
    }

    @PostMapping("/signUp")
    public String signUp(Model model, @ModelAttribute User user) {
        Optional<User> signUpUser = userService.add(user);
        if (signUpUser.isEmpty()) {
            model.addAttribute(
                    "message",
                    "An account already exists. Please use log in option");
            return "errors/404";
        }
        model.addAttribute("message",
                "Successfully signed up. Please log in if you wish to buy a ticket");
        return "success/signUpSuccess";
    }

    @GetMapping("/formLoginUser")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(Model model, @ModelAttribute User user, HttpServletRequest req) {
        Optional<User> loginUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (loginUser.isEmpty()) {
            model.addAttribute(
                    "message", "Incorrect input details or account does not exist");
            return "errors/404";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", loginUser.get());
        return "redirect:/filmSessions";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/formLoginUser";
    }
}
