package ru.ange.mhb.pojo.movie;

import ru.ange.mhb.utils.Constants;

import java.util.Calendar;
import java.util.Date;

public class SearchMovie extends Movie {

    private Date releaseDate;

    public SearchMovie(String name, int tmdbId) {
        super(name, tmdbId);
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public SearchMovie setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    private int getYear() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(releaseDate);
            int year = cal.get(Calendar.YEAR);
            return year;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public String getTitle() {
        String year = getYear() > 0 ? String.format(Constants.MOVIES_PARAMS, getYear()) : "";
        String title = getName() + " " + year;
        return title;
    }

}
