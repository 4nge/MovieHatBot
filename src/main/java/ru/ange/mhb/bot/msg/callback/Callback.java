package ru.ange.mhb.bot.msg.callback;


// TODO create serializer to convert boolean to int
public class Callback {

    private String nm; // name

    public Callback(String name) {
        this.nm = name;
    }

    public String getNm() {
        return nm;
    }

    public boolean equals(Callback callback) {
        return this.getNm() != null && callback.getNm() != null && callback.getNm().equals(getNm());
    }
}
