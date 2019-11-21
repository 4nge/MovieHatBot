package ru.ange.mhb.pojo.movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesPage {

    private int pageIdx;
    private int totalPages;
    private int totalMovies;
    private List<SearchMovie> movies;

    public MoviesPage(int pageIdx, int totalPages, int totalMovies) {
        this(pageIdx, totalPages, totalMovies, new ArrayList<>());
    }

    public MoviesPage(int pageIdx, int totalPages, int totalMovies, List<SearchMovie> movies) {
        this.pageIdx = pageIdx;
        this.totalPages = totalPages;
        this.totalMovies = totalMovies;
        this.movies = movies;
    }

    public int getPageIdx() {
        return pageIdx;
    }

    public void setPageIdx(int pageIdx) {
        this.pageIdx = pageIdx;
    }

    public List<SearchMovie> getMovies() {
        return movies;
    }

    public void setMovies(List<SearchMovie> movies) {
        this.movies = movies;
    }

    public void addMovie(SearchMovie movie) {
        this.movies.add( movie );
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(int totalMovies) {
        this.totalMovies = totalMovies;
    }

    @Override
    public String toString() {
        return "MoviesPage{" +
                "pageIdx=" + pageIdx +
                ", totalPages=" + totalPages +
                ", totalMovies=" + totalMovies +
                '}';
    }
}
