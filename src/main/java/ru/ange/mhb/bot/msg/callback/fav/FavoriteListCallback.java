package ru.ange.mhb.bot.msg.callback.fav;

import ru.ange.mhb.bot.msg.callback.CallbackWithFavListId;

public abstract class FavoriteListCallback extends CallbackWithFavListId {

    private boolean wtchd;
    private boolean edit;

    // TODO private ?
    protected FavoriteListCallback(String name) {
        super(name);
    }

    public FavoriteListCallback(String name, int flsId, boolean watched, boolean edit) {
        super(name, flsId);
        this.wtchd = watched;
        this.edit = edit;
    }

    public boolean isWtchd() {
        return wtchd;
    }

    public FavoriteListCallback setWtchd(boolean wtchd) {
        this.wtchd = wtchd;
        return this;
    }

    public boolean isEdit() {
        return edit;
    }

    public FavoriteListCallback setEdit(boolean edit) {
        this.edit = edit;
        return this;
    }

    @Override
    public String toString() {
        return "FavoriteListCallback{" +
                "nm=" + getNm() + "," +
                "fLsId=" + getFLsId() + "," +
                "watched=" + wtchd + "," +
                "edit=" + edit +
                '}';
    }
}
