package ru.ange.mhb.pojo.movie;

public class MovieCrewPerson {

    private int tmdbId;
    public String name;
    public String localizedName;

    public MovieCrewPerson(int tmdbId, String name) {
        this.tmdbId = tmdbId;
        this.name = name;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }
}
