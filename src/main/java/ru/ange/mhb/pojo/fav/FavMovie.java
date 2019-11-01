package ru.ange.mhb.pojo.fav;

import ru.ange.mhb.pojo.movie.Movie;

import java.util.Date;

public class FavMovie extends Movie {

    private int id;
    private Date addDate;
    private int favListId;
    private int addUserId;
    private boolean watched;

    public FavMovie(String name, int tmdbId) {
        super(name, tmdbId);
    }

    public FavMovie(String name, int tmdbId, Date addDate, int favListId, int addUserId, boolean watched) {
        super(name, tmdbId);
        this.addDate = addDate;
        this.favListId = favListId;
        this.addUserId = addUserId;
        this.watched = watched;
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
        return watched;
    }

    public FavMovie setWatched(boolean watched) {
        this.watched = watched;
        return this;
    }
}
