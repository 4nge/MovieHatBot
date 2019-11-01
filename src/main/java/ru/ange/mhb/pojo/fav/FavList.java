package ru.ange.mhb.pojo.fav;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavList {

    private int id;
    private String name;
    private Date date;
    private int createUserId;
    private boolean publ;
    private List<FavMovie> favMovies;

    public FavList(int id, String name, Date date, int createUserId, boolean publ) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.createUserId = createUserId;
        this.publ = publ;
        this.favMovies = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public FavList setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FavList setName(String name) {
        this.name = name;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public FavList setDate(Date date) {
        this.date = date;
        return this;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public FavList setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
        return this;
    }

    public boolean isPubl() {
        return publ;
    }

    public FavList setPubl(boolean publ) {
        this.publ = publ;
        return this;
    }

    public List<FavMovie> getFavMovies() {
        return favMovies;
    }

    public FavList setFavMovies(List<FavMovie> favMovies) {
        this.favMovies = favMovies;
        return this;
    }

    @Override
    public String toString() {
        return "FavList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", createUserId=" + createUserId +
                ", publ=" + publ +
                '}';
    }
}
