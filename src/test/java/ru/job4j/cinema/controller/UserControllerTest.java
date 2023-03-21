package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;
    private HttpServletRequest request;
    private HttpSession session;

    @BeforeEach
    public void preLoad() {
        userService = mock(UserService.class);
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestFormAddUserThenGetUserAddUser() {
        var view = userController.addUser();
        assertThat(view).isEqualTo("user/addUser");
    }

    @Test
    public void whenPostSignUpThenGetSuccessSignUpSuccess() {
        User newUser = new User(
                1,
                "Bob",
                "bob@bob.com",
                "bobik"
                );
        var argUser = ArgumentCaptor.forClass(User.class);
        when(userService.addUser(argUser.capture())).thenReturn(Optional.of(newUser));
        var model = new ConcurrentModel();
        var view = userController.signUp(model, newUser);
        var actualUser = argUser.getValue();
        assertThat(view).isEqualTo("success/signUpSuccess");
        assertThat(actualUser).isEqualTo(newUser);
    }

    @Test
    public void whenPostSignUpWithExistingAccountThenGetErrors404() {
        User newUser = new User(
                1,
                "Bob",
                "bob@bob.com",
                "bobik"
        );
        when(userService.addUser(newUser)).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = userController.signUp(model, newUser);
        assertThat(view).isEqualTo("errors/404");
    }

    @Test
    public void whenRequestFormLoginUserThenGetUserLogin() {
        var view = userController.login();
        assertThat(view).isEqualTo("user/login");
    }

    @Test
    public void whenPostLoginThenRedirectFilmSessions() {
        User user = new User(
                1,
                "Bob",
                "bob@bob.com",
                "bobik"
        );
        Optional<User> loginUser = Optional.of(user);
        when(userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(loginUser);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("user")).thenReturn(loginUser.get());
        var model = new ConcurrentModel();
        var view = userController.login(model, user, request);
        assertThat(view).isEqualTo("redirect:/filmSessions");
        assertThat(request.getSession().getAttribute("user")).isEqualTo(user);
    }

    @Test
    public void whenRequestLogoutThenRedirectFormLoginUser() {
        var view = userController.logout(session);
        assertThat(view).isEqualTo("redirect:/formLoginUser");
    }

}
