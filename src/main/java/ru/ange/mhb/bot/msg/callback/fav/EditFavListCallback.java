package ru.ange.mhb.bot.msg.callback.fav;

public class EditFavListCallback extends FavoriteListCallback {

    public static final String NAME = "edit_fv_cb";

    private EditFavListCallback() {
        super(NAME);
    }

    public EditFavListCallback(int favListId, boolean showWatched, boolean editMode) {
        super(NAME, favListId, showWatched, editMode);
    }

}
