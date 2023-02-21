package ru.job4j.cinema.model;

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
    private String startTime;
    private String endTime;

    public FilmSession() {

    }

    public FilmSession(int id, int filmId, int hallsId, String startTime, String endTime) {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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
                + ", start_time='" + startTime + '\''
                + ", end_time='" + endTime + '\''
                + '}';
    }
}
