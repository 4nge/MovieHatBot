package ru.ange.mhb.bot.msg.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class ChoiceFavListCallback extends Callback {

    public static final String NAME = "chFavLsCll";

    private int movieId;
    private int favListId;

    private ChoiceFavListCallback() {
        super(NAME);
    }

    public ChoiceFavListCallback(int movieId, int favListId) {
        this();
        this.movieId = movieId;
        this.favListId = favListId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getFavListId() {
        return favListId;
    }

    public void setFavListId(int favListId) {
        this.favListId = favListId;
    }
}
