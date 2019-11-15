package ru.ange.mhb.bot.msg.callback.movie;

import ru.ange.mhb.bot.msg.callback.Callback;

// TODO
public class BackToMovieInfoCallback extends Callback {

    public static final String NAME = "chFavLsBcCll";

    private int movieId;

    private BackToMovieInfoCallback() {
        super(NAME);
    }

    public BackToMovieInfoCallback(int movieId) {
        this();
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "BackToMovieInfoCallback{" +
                "movieId=" + movieId +
                '}';
    }
}
