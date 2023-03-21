package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.model.File;

import java.util.Optional;

public interface FileService {

    Optional<File> addFile(File file);
    Optional<File> findByFileId(int id);
    boolean deleteByFileId(int id);
    Optional<FileDto> getFileById(int id);
}
