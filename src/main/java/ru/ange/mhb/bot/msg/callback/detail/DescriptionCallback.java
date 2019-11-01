package ru.ange.mhb.bot.msg.callback.detail;

import ru.ange.mhb.bot.msg.callback.Callback;

public class DescriptionCallback extends Callback {

    public static final String NAME = "mb_desc_cb";

    private int tmdbMovieId;

    private DescriptionCallback() {
        super(NAME);
    }

    public DescriptionCallback(int tmdbMovieId) {
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
        return "DescriptionCallback{" +
                "tmdbMovieId=" + tmdbMovieId +
                '}';
    }

}
