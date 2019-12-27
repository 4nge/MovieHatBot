package ru.ange.mhb.bot.msg.callback.fav;

public class ChoiceShowingFavListToCallback extends FavoriteListCallback {

    public static final String NAME = "ch_fvl_cb";

    private ChoiceShowingFavListToCallback() {
        super(NAME);
    }

    public ChoiceShowingFavListToCallback(int favListId, boolean showWatched, boolean editMode) {
        super(NAME, favListId, showWatched, editMode);
    }

}
