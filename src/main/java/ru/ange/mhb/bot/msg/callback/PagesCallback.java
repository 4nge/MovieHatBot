package ru.ange.mhb.bot.msg.callback;

public class PagesCallback extends Callback {

    private static final String NAME = "pages_cb";
    private int pageNumber;

    private PagesCallback() {
        super(NAME);
    }

    public PagesCallback(int pageNumber) {
        this();
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return "PagesCallback{" +
                "page=" + pageNumber +
                '}';
    }
}
