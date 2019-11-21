package ru.ange.mhb.bot.msg.callback.fav;

import ru.ange.mhb.bot.msg.callback.CallbackWithFavListId;

public class ChoiceShowingFavListToCallback extends CallbackWithFavListIdAndShowWatched {

    public static final String NAME = "ch_fvl_cb";

    private ChoiceShowingFavListToCallback() {
        super(NAME);
    }

    public ChoiceShowingFavListToCallback(int favListId, boolean showWatched) {
        super(NAME, favListId, showWatched);
    }

}
