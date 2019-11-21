package ru.ange.mhb.bot.msg.callback.fav;

import ru.ange.mhb.bot.msg.callback.CallbackWithFavListId;

public abstract class CallbackWithFavListIdAndShowWatched extends CallbackWithFavListId {

    private boolean showWatched;

    protected CallbackWithFavListIdAndShowWatched(String name) {
        super(name);
    }

    public CallbackWithFavListIdAndShowWatched(String name, int favListdId, boolean showWatched) {
        super(name, favListdId);
        this.showWatched = showWatched;
    }

    public boolean isShowWatched() {
        return showWatched;
    }

    public CallbackWithFavListIdAndShowWatched setShowWatched(boolean showWatched) {
        this.showWatched = showWatched;
        return this;
    }

    @Override
    public String toString() {
        return "CallbackWithFavListIdAndShowWatched{" +
                "showWatched=" + showWatched +
                "favListdId=" + getFavListdId() +
                '}';
    }
}
