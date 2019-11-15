package ru.ange.mhb.bot.msg.callback.movie.detail;

import ru.ange.mhb.bot.msg.callback.CallbackWithTmdbId;

public class DescriptionCallback extends CallbackWithTmdbId {

    public static final String NAME = "mb_desc_cb";

    private DescriptionCallback() {
        super(NAME);
    }

    public DescriptionCallback(int tmdbMovieId) {
        super(NAME, tmdbMovieId);
    }

}
