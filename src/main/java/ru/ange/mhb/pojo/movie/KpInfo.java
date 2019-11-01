package ru.ange.mhb.pojo.movie;

public class KpInfo {

    private int id;
    private float kpRating;
    private float IMDbRating;
    private String link;

    public KpInfo(int id, String link) {
        this.id = id;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getKpRating() {
        return kpRating;
    }

    public void setKpRating(float kpRating) {
        this.kpRating = kpRating;
    }

    public float getIMDbRating() {
        return IMDbRating;
    }

    public void setIMDbRating(float IMDbRating) {
        this.IMDbRating = IMDbRating;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "KpInfo{" +
                "id=" + id +
                ", kpRating=" + kpRating +
                ", IMDbRating=" + IMDbRating +
                ", link='" + link + '\'' +
                '}';
    }
}
