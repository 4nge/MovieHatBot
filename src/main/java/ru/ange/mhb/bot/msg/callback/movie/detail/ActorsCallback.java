package ru.ange.mhb.bot.msg.callback.movie.detail;

import ru.ange.mhb.bot.msg.callback.CallbackWithTmdbId;

public class ActorsCallback extends CallbackWithTmdbId {

    public static final String NAME = "mb_acts_cb";

    private ActorsCallback() {
        super(NAME);
    }

    public ActorsCallback(int tmdbMovieId) {
        super(NAME, tmdbMovieId);
    }

}
