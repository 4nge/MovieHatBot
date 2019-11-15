package ru.ange.mhb.bot.msg.callback.fav;


import ru.ange.mhb.bot.msg.callback.Callback;

// TODO
public class AddToFavoriteCallback extends Callback {

    public static final String NAME = "add_to_fav_cb";
    private int movieId;

    private AddToFavoriteCallback() {
        super(NAME);
    }

    public AddToFavoriteCallback(int movieId) {
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
        return "AddToFavoriteCallback{" +
                "movieId=" + movieId +
                '}';
    }
}
