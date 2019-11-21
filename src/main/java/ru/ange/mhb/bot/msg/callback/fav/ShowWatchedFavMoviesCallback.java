package ru.ange.mhb.bot.msg.callback.fav;

import ru.ange.mhb.bot.msg.callback.CallbackWithFavListId;

public class ShowWatchedFavMoviesCallback extends CallbackWithFavListIdAndShowWatched {

    public static final String NAME = "show_watched_cb";

    private ShowWatchedFavMoviesCallback() {
        super(NAME);
    }

    public ShowWatchedFavMoviesCallback(int favListId, boolean showWatched) {
        super(NAME, favListId, showWatched);
    }

}
