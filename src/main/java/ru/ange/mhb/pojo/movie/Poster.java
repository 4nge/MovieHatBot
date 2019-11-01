package ru.ange.mhb.pojo.movie;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Poster {

    private String path;
    private String name;
    //private BufferedImage bufferedImage;

    public Poster(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public Poster setPath(String path) {
        this.path = path;
        return this;
    }

    public String getName() {
        return name;
    }

    public Poster setName(String name) {
        this.name = name;
        return this;
    }

    public String getFullPath() {
        return path + name;
    }

    public BufferedImage getBufferedImage() {
        try {
            return ImageIO.read(new URL(getFullPath()));
        } catch (IOException e) {
            return null;
        }
    }
}
