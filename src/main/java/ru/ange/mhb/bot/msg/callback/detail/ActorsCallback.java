package ru.ange.mhb.bot.msg.callback.detail;

import ru.ange.mhb.bot.msg.callback.Callback;

public class ActorsCallback extends Callback {

    public static final String NAME = "mb_acts_cb";

    private int tmdbMovieId;

    private ActorsCallback() {
        super(NAME);
    }

    public ActorsCallback(int tmdbMovieId) {
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
        return "ActorsCallback{" +
                "tmdbMovieId=" + tmdbMovieId +
                '}';
    }

}
