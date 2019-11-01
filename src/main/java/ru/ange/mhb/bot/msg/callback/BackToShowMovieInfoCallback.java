package ru.ange.mhb.bot.msg.callback;

public class BackToShowMovieInfoCallback extends Callback {

    public static final String NAME = "chFavLsBcCll";

    private int movieId;

    private BackToShowMovieInfoCallback() {
        super(NAME);
    }

    public BackToShowMovieInfoCallback(int movieId) {
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
        return "BackToShowMovieInfoCallback{" +
                "movieId=" + movieId +
                '}';
    }
}
