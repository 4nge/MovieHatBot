package ru.ange.mhb.bot.msg.callback;


public abstract class CallbackWithTmdbId extends Callback {

    private int tmdbMovieId;

    protected CallbackWithTmdbId(String name) {
        super(name);
    }

    public CallbackWithTmdbId(String name, int tmdbMovieId) {
        this(name);
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
        return "CallbackWithTmdbId{" +
                "name=" + getName() +
                "tmdbMovieId=" + tmdbMovieId +
                '}';
    }
}
