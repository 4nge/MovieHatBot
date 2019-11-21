package ru.ange.mhb.bot.msg.callback.fav;

import ru.ange.mhb.bot.msg.callback.Callback;

public class ChoiceFavListToAddMovieCallback extends Callback {

    public static final String NAME = "chFavLsCll";

    private int movieId;
    private int favListId;

    private ChoiceFavListToAddMovieCallback() {
        super(NAME);
    }

    public ChoiceFavListToAddMovieCallback(int movieId, int favListId) {
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
