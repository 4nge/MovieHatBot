package ru.ange.mhb.bot.msg.callback.detail;

import ru.ange.mhb.bot.msg.callback.Callback;

public class MoreDetailsCallback extends Callback {

    public static final String NAME = "more_det_cb";

    private int tmdbMovieId;

    private MoreDetailsCallback() {
        super(NAME);
    }

    public MoreDetailsCallback(int tmdbMovieId) {
        this();
        this.tmdbMovieId = tmdbMovieId;
    }

    public int getTmdbMovieId() {
        return tmdbMovieId;
    }

    public void setTmdbMovieId(int tmdbMovieId) {
        this.tmdbMovieId = tmdbMovieId;
    }

    @Override
    public String toString() {
        return "MoreDetailsCallback{" +
                "tmdbMovieId=" + tmdbMovieId +
                '}';
    }
}
