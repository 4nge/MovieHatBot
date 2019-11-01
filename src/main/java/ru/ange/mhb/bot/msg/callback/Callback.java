package ru.ange.mhb.bot.msg.callback;

public class Callback {

    private String name;

    public Callback(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Callback callback) {
        return this.getName() != null && callback.getName() != null && callback.getName().equals(getName());
    }
}
