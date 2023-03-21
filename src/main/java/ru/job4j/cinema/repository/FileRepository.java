package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.File;

import java.util.Optional;

public interface FileRepository {

    Optional<File> addFile(File file);
    Optional<File> findByFileId(int id);
    boolean deleteByFileId(int id);
}
