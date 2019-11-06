package ru.ange.mhb.bot.msg.callback.detail;

import ru.ange.mhb.bot.msg.callback.Callback;

public class SetRatingCallback extends Callback {

    public static final String NAME = "set_rat_cb";

    private int favMovieId;
    private int rating;

    private SetRatingCallback() {
        super(NAME);
    }

    public SetRatingCallback(int favMovieId, int rating) {
        this();
        this.favMovieId = favMovieId;
        this.rating = rating;
    }

    public int getFavMovieId() {
        return favMovieId;
    }

    public SetRatingCallback setFavMovieId(int favMovieId) {
        this.favMovieId = favMovieId;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public SetRatingCallback setRating(int rating) {
        this.rating = rating;
        return this;
    }

    @Override
    public String toString() {
        return "SetRatingCallback{" +
                "favMovieId=" + favMovieId +
                ", rating=" + rating +
                '}';
    }
}
