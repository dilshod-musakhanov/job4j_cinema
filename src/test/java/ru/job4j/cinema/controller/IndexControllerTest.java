package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.service.HallService;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IndexControllerTest {

    private HallService hallService;
    private IndexController indexController;

    @BeforeEach
    public void preLoad() {
        hallService = mock(HallService.class);
        indexController = new IndexController(hallService);
    }

    @Test
    public void whenRequestSlashForwardOrIndexThenGetIndexIndex() {
        Collection<Hall> halls = List.of(
                new Hall(1, "SKY", 5, 50, "for couples"),
                new Hall(2, "ISLAND", 8, 100, "for families")
        );
        when(hallService.findAll()).thenReturn(halls);
        var model = new ConcurrentModel();
        var view = indexController.mainPageWithHalls(model);
        assertThat(view).isEqualTo("index/index");
        assertThat(model.getAttribute("halls")).isEqualTo(halls);

    }
}
