package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.service.FileService;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileControllerTest {

    private FileService fileService;
    private FileController fileController;
    private MultipartFile testFile;

    @BeforeEach
    public void preLoad() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
        testFile = new MockMultipartFile("test.img", new byte[] {1, 2, 3});
    }

    @Test
    public void whenRequestByFileIdThenGetFileContent() throws IOException {
        var fileDto = new FileDto(testFile.getName(), testFile.getBytes());
        when(fileService.getFileById(1)).thenReturn(Optional.ofNullable(fileDto));
        var actualFileDto = fileController.getById(1);
        assertThat(actualFileDto.getBody()).isEqualTo(fileDto.getContent());
    }

}
