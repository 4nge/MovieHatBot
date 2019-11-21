package ru.ange.mhb.bot.msg.callback;

public abstract class CallbackWithFavListId extends Callback {

    private int favListdId;

    protected CallbackWithFavListId(String name) {
        super(name);
    }

    public CallbackWithFavListId(String name, int favListdId) {
        this(name);
        this.favListdId = favListdId;
    }

    public int getFavListdId() {
        return favListdId;
    }

    public CallbackWithFavListId setFavListdId(int favListdId) {
        this.favListdId = favListdId;
        return this;
    }

    @Override
    public String toString() {
        return "CallbackWithFavListId{" +
                "favListdId=" + favListdId +
                '}';
    }
}
