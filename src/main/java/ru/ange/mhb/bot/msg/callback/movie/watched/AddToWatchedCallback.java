package ru.ange.mhb.bot.msg.callback.movie.watched;

import ru.ange.mhb.bot.msg.callback.CallbackWithFavMovieId;

public class AddToWatchedCallback extends CallbackWithFavMovieId {

    public static final String NAME = "add_to_watched_cb";

    private AddToWatchedCallback() {
        super(NAME);
    }

    public AddToWatchedCallback(int favMoviedId) {
        super(NAME, favMoviedId);
    }
}
