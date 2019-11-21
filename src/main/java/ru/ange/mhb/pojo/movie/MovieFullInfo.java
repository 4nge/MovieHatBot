package ru.ange.mhb.pojo.movie;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MovieFullInfo extends SearchMovie {

    private String desc;
    private float rating;
    private KpInfo kpInfo;
    private Poster poster;
    private MovieCrewPerson director;
    private List<MovieCrewPerson> actors;
    private List<Genre> genres;
    private String wikiLink;

    private long burget;
    private String tagline;
    private long revenue;
    private String originalTitle;
    private int duration;
    private MovieCrewPerson composer;
    private MovieCrewPerson photographer;
    private List<MovieCrewPerson> screenwriters;
    private List<MovieCrewPerson> producers;
    private List<String> countries;

    public MovieFullInfo(String name, int tmdbId) {
        super( name, tmdbId );
        this.actors = new ArrayList<MovieCrewPerson>();
        this.genres = new ArrayList<Genre>();
        this.producers = new ArrayList<MovieCrewPerson>();
        this.screenwriters = new ArrayList<>();
        this.countries = new ArrayList<>();
    }

    public Poster getPoster() {
        return poster;
    }

    public MovieFullInfo setPoster(Poster poster) {
        this.poster = poster;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public MovieFullInfo setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public MovieFullInfo setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public KpInfo getKpInfo() {
        return kpInfo;
    }

    public MovieFullInfo setKpInfo(KpInfo kpInfo) {
        this.kpInfo = kpInfo;
        return this;
    }

    public MovieCrewPerson getDirector() {
        return director;
    }

    public MovieFullInfo setDirector(MovieCrewPerson director) {
        this.director = director;
        return this;
    }

    public List<MovieCrewPerson> getActors() {
        return actors;
    }

    public MovieFullInfo setActors(List<MovieCrewPerson> actors) {
        this.actors = actors;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public MovieFullInfo setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public String getWikiLink() {
        return wikiLink;
    }

    public MovieFullInfo setWikiLink(String wikiLink) {
        this.wikiLink = wikiLink;
        return this;
    }

    public long getBurget() {
        return burget;
    }

    public MovieFullInfo setBurget(long burget) {
        this.burget = burget;
        return this;
    }

    public String getTagline() {
        return tagline;
    }

    public MovieFullInfo setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public long getRevenue() {
        return revenue;
    }

    public MovieFullInfo setRevenue(long revenue) {
        this.revenue = revenue;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public MovieFullInfo setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public MovieFullInfo setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public List<MovieCrewPerson> getProducers() {
        return producers;
    }

    public MovieFullInfo setProducers(List<MovieCrewPerson> producers) {
        this.producers = producers;
        return this;
    }

    public MovieCrewPerson getComposer() {
        return composer;
    }

    public MovieFullInfo setComposer(MovieCrewPerson composer) {
        this.composer = composer;
        return this;
    }

    public List<MovieCrewPerson> getScreenwriters() {
        return screenwriters;
    }

    public MovieFullInfo setScreenwriters(List<MovieCrewPerson> screenwriters) {
        this.screenwriters = screenwriters;
        return this;
    }

    public MovieCrewPerson getPhotographer() {
        return photographer;
    }

    public MovieFullInfo setPhotographer(MovieCrewPerson photographer) {
        this.photographer = photographer;
        return this;
    }

    public List<String> getCountries() {
        return countries;
    }

    public MovieFullInfo setCountries(List<String> countries) {
        this.countries = countries;
        return this;
    }

    public MovieFullInfo setReleaseDate(Date releaseDate) {
        super.setReleaseDate(releaseDate);
        return this;
    }

    @Override
    public String toString() {
        return "MovieFullInfo{" +
                "desc='" + desc + '\'' +
                ", rating=" + rating +
                ", kpInfo=" + kpInfo +
                ", poster=" + poster +
                ", director=" + director +
                ", actors=" + actors +
                ", genres=" + genres +
                ", wikiLink='" + wikiLink + '\'' +
                ", burget=" + burget +
                ", tagline='" + tagline + '\'' +
                ", revenue=" + revenue +
                ", originalTitle='" + originalTitle + '\'' +
                ", duration=" + duration +
                ", composer=" + composer +
                ", photographer=" + photographer +
                ", screenwriters=" + screenwriters +
                ", producers=" + producers +
                ", countries=" + countries +
                '}';
    }
}
