package ru.job4j.cinema.dto;


import java.time.LocalDateTime;
import java.util.Objects;

public class FilmSessionDto {

    private int id;
    private String film;
    private String hall;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public FilmSessionDto() {
    }

    public FilmSessionDto(int id, String film, String hall, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.film = film;
        this.hall = hall;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
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
        if (!(o instanceof FilmSessionDto)) {
            return false;
        }
        FilmSessionDto fsd = (FilmSessionDto) o;
        return getId() == fsd.getId() && Objects.equals(getFilm(), fsd.getFilm()) && Objects.equals(getHall(), fsd.getHall());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFilm(), getHall());
    }

    @Override
    public String toString() {
        return "FilmSessionDto{"
                + "id=" + id
                + ", film='" + film + '\''
                + ", hall='" + hall + '\''
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + '}';
    }
}
