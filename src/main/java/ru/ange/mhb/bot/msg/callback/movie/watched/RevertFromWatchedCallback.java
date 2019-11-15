package ru.ange.mhb.bot.msg.callback.movie.watched;


import ru.ange.mhb.bot.msg.callback.CallbackWithFavMovieId;

public class RevertFromWatchedCallback extends CallbackWithFavMovieId {

    public static final String NAME = "revert_from_watched_cb";

    private RevertFromWatchedCallback() {
        super(NAME);
    }

    public RevertFromWatchedCallback(int favMoviedId) {
        super(NAME, favMoviedId);
    }
}
