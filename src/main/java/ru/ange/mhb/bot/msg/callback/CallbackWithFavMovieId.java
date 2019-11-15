package ru.ange.mhb.bot.msg.callback;

public abstract class CallbackWithFavMovieId extends Callback {

    private int favMoviedId;

    protected CallbackWithFavMovieId(String name) {
        super(name);
    }

    public CallbackWithFavMovieId(String name, int favMoviedId) {
        this(name);
        this.favMoviedId = favMoviedId;
    }

    public int getFavMoviedId() {
        return favMoviedId;
    }

    public CallbackWithFavMovieId setFavMoviedId(int favMoviedId) {
        this.favMoviedId = favMoviedId;
        return this;
    }

    @Override
    public String toString() {
        return "CallbackWithFavMovieId{" +
                "favMoviedId=" + favMoviedId +
                '}';
    }
}
