package ru.ange.mhb.bot.msg.callback.movie;


import ru.ange.mhb.bot.msg.callback.CallbackWithFavMovieId;

public class SetRatingCallback extends CallbackWithFavMovieId {

    public static final String NAME = "set_rat_cb";

    private int rating;

    private SetRatingCallback() {
        super(NAME);
    }

    public SetRatingCallback(int favMovieId, int rating) {
        super(NAME, favMovieId);
        this.rating = rating;
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
                "favMovieId=" + getFavMoviedId() +
                ", rating=" + rating +
                '}';
    }
}
