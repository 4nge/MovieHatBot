package ru.ange.mhb.bot.msg.callback.movie.detail;

import ru.ange.mhb.bot.msg.callback.CallbackWithTmdbId;

public class CrewCallback extends CallbackWithTmdbId {

    public static final String NAME = "mb_crew_cb";

    private CrewCallback() {
        super(NAME);
    }

    public CrewCallback(int tmdbMovieId) {
        super(NAME, tmdbMovieId);
    }

}
