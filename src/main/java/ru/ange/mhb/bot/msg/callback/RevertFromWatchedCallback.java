package ru.ange.mhb.bot.msg.callback;


public class RevertFromWatchedCallback extends Callback {

    public static final String NAME = "revert_from_watched_cb";
    private int favMovieId;

    private RevertFromWatchedCallback() {
        super(NAME);
    }

    public RevertFromWatchedCallback(int favMovieId) {
        this();
        this.favMovieId = favMovieId;
    }

    public int getFavMovieId() {
        return favMovieId;
    }

    public void setFavMovieId(int favMovieId) {
        this.favMovieId = favMovieId;
    }

    @Override
    public String toString() {
        return "RevertFromWatchedCallback{" +
                "favMovieId=" + favMovieId +
                '}';
    }
}
