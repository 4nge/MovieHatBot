package ru.ange.mhb.bot.msg.callback.detail;

import ru.ange.mhb.bot.msg.callback.Callback;

public class CrewCallback extends Callback {

    public static final String NAME = "mb_crew_cb";

    private int tmdbMovieId;

    private CrewCallback() {
        super(NAME);
    }

    public CrewCallback(int tmdbMovieId) {
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
        return "CrewCallback{" +
                "tmdbMovieId=" + tmdbMovieId +
                '}';
    }

}
