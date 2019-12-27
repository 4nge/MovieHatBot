package ru.ange.mhb.bot.msg.callback.fav;

import ru.ange.mhb.bot.msg.callback.CallbackWithFavListId;

public class DeleteConfirmFavListCallback extends FavoriteListCallback {

    public static final String NAME = "del_c_fv_cb";

    protected DeleteConfirmFavListCallback() {
        super(NAME);
    }

    public DeleteConfirmFavListCallback(int fLsId, boolean watched) {
        super(NAME, fLsId, watched, false);
    }

}
