package ru.ange.mhb.pojo.movie;

public class SearchMovie extends Movie {

    private int year;

    public SearchMovie(String name, int year, int tmdbId) {
        super(name, tmdbId);
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public SearchMovie setYear(int year) {
        this.year = year;
        return this;
    }
}
