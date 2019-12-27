package ru.ange.mhb.bot.msg.callback.fav;

public class ShowWatchedFavMoviesCallback extends FavoriteListCallback {

    public static final String NAME = "watched_cb";

    private ShowWatchedFavMoviesCallback() {
        super(NAME);
    }

    public ShowWatchedFavMoviesCallback(int flsId, boolean showWatched, boolean editMode) {
        super(NAME, flsId, showWatched, editMode);
    }

    @Override
    public String toString() {
        return "ShowWatchedFavMoviesCallback{" +
                "nm=" + getNm() + "," +
                "flsId=" + getFLsId() + "," +
                "watched=" + isWtchd() + "," +
                "edit=" + isEdit() +
                "}";
    }
}
