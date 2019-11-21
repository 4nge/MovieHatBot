package ru.ange.mhb.pojo.user;

public class BotUserBasic {

    private int id;
    private int telUserId;
    private String alias;
    private String language;
    private boolean adult;

    public BotUserBasic(int id, int telUserId, String alias, String language, boolean adult) {
        this.id = id;
        this.telUserId = telUserId;
        this.alias = alias;
        this.language = language;
        this.adult = adult;
    }

    public int getId() {
        return id;
    }

    public BotUserBasic setId(int id) {
        this.id = id;
        return this;
    }

    public int getTelUserId() {
        return telUserId;
    }

    public BotUserBasic setTelUserId(int telUserId) {
        this.telUserId = telUserId;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public BotUserBasic setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public BotUserBasic setLanguage(String language) {
        this.language = language;
        return this;
    }

    public boolean isAdult() {
        return adult;
    }

    public BotUserBasic setAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    @Override
    public String toString() {
        return "BotUserBasic{" +
                "id=" + id +
                ", telUserId=" + telUserId +
                ", alias='" + alias + '\'' +
                ", language='" + language + '\'' +
                ", adult=" + adult +
                '}';
    }
}
