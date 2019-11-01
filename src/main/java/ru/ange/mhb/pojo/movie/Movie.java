package ru.ange.mhb.pojo.movie;

import java.util.Objects;

public class Movie {

    private String name;
    private int tmdbId;

    public Movie(String name, int tmdbId) {
        this.name = name;
        this.tmdbId = tmdbId;
    }

    public String getName() {
        return name;
    }

    public Movie setName(String name) {
        this.name = name;
        return this;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public Movie setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return this.tmdbId == movie.tmdbId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tmdbId);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", tmdbId=" + tmdbId +
                '}';
    }
}
