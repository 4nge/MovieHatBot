package ru.ange.mhb.bot.msg.callback.fav;

public class DeleteFavListCallback extends FavoriteListCallback {

    public static final String NAME = "del_fv_cb";

    protected DeleteFavListCallback() {
        super(NAME);
    }

    public DeleteFavListCallback(int fLsId, boolean watched) {
        super(NAME, fLsId, watched, true);
    }

}
