package ru.ange.mhb.pojo.fav;

import ru.ange.mhb.pojo.movie.Movie;
import ru.ange.mhb.pojo.movie.SearchMovie;

import java.util.Date;

public class FavMovie extends Movie {

    private int id;
    private Date addDate;
    private int favListId;
    private int addUserId;
    private Date watchedDate;
    private Integer rating;

    public FavMovie(String name, int tmdbId) {
        super(name, tmdbId);
    }


    public int getFavListId() {
        return favListId;
    }

    public FavMovie setFavListId(int favListId) {
        this.favListId = favListId;
        return this;
    }

    public int getId() {
        return id;
    }

    public FavMovie setId(int id) {
        this.id = id;
        return this;
    }

    public Date getAddDate() {
        return addDate;
    }

    public FavMovie setAddDate(Date addDate) {
        this.addDate = addDate;
        return this;
    }

    public int getAddUserId() {
        return addUserId;
    }

    public FavMovie setAddUserId(int addUserId) {
        this.addUserId = addUserId;
        return this;
    }

    public boolean isWatched() {
        return watchedDate != null;
    }

    public Integer getRating() {
        return rating;
    }

    public FavMovie setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public Date getWatchedDate() {
        return watchedDate;
    }

    public FavMovie setWatchedDate(Date watchedDate) {
        this.watchedDate = watchedDate;
        return this;
    }
}
