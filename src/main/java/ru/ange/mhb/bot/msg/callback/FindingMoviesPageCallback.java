package ru.ange.mhb.bot.msg.callback;

public class FindingMoviesPageCallback {

    public static final String NAME = "fmv_page_callback";

    public FindingMoviesPageCallback() {}
    public FindingMoviesPageCallback(int i ) {}

    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "FindingMoviesPageCallback{" +
                "name='" + getName() + '\'' +
                '}';
    }
}
