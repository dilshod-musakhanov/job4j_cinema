package ru.job4j.cinema.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class FilmSession {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "film_id", "filmId",
            "halls_id", "hallsId",
            "start_time", "startTime",
            "end_time", "endTime"
    );

    private int id;
    private int filmId;
    private int hallsId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public FilmSession() {

    }

    public FilmSession(int id, int filmId, int hallsId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.filmId = filmId;
        this.hallsId = hallsId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getHallsId() {
        return hallsId;
    }

    public void setHallsId(int hallsId) {
        this.hallsId = hallsId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilmSession)) {
            return false;
        }
        FilmSession fs = (FilmSession) o;
        return getId() == fs.getId() && getFilmId() == fs.getFilmId() && getHallsId() == fs.getHallsId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFilmId(), getHallsId());
    }

    @Override
    public String toString() {
        return "FilmSession{"
                + "id=" + id
                + ", filmId=" + filmId
                + ", hallsId=" + hallsId
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + '}';
    }
}
