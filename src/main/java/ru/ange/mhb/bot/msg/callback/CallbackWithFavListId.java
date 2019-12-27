package ru.ange.mhb.bot.msg.callback;

public abstract class CallbackWithFavListId extends Callback {

    private int fLsId;

    protected CallbackWithFavListId(String name) {
        super(name);
    }

    public CallbackWithFavListId(String name, int fLsId) {
        this(name);
        this.fLsId = fLsId;
    }

    public int getFLsId() {
        return fLsId;
    }

    public void setFLsId(int fLsId) {
        this.fLsId = fLsId;
    }

    @Override
    public String toString() {
        return "CallbackWithFavListId{" +
                "fLsId=" + fLsId +
                '}';
    }
}
