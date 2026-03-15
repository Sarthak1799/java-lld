package models;

import java.util.*;

public class Artist {

    private String name;
    private String genre;
    private String id;

    public Artist(String name, String genre) {
        this.name = name;
        this.genre = genre;
        this.id = "artist-" + UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getId() {
        return id;
    }
}
