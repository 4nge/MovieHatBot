package ru.ange.mhb.bot.msg.callback.movie.detail;


import ru.ange.mhb.bot.msg.callback.CallbackWithTmdbId;

public class MoreDetailsCallback extends CallbackWithTmdbId {

    public static final String NAME = "more_det_cb";

    private MoreDetailsCallback() {
        super(NAME);
    }

    public MoreDetailsCallback(int tmdbMovieId) {
        super(NAME, tmdbMovieId);
    }

}
