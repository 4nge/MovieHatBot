package ru.ange.mhb.bot.msg.callback;


public class AddToWatchedCallback extends Callback {

    public static final String NAME = "add_to_watched_cb";
    private int favMovieId;

    private AddToWatchedCallback() {
        super(NAME);
    }

    public AddToWatchedCallback(int favMovieId) {
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
        return "AddToWatchedCallback{" +
                "favMovieId=" + favMovieId +
                '}';
    }
}
