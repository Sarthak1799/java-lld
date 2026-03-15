package models;

import java.util.*;

public class Album {

    private String name;
    private String artistId;
    private int releaseYear;
    private String id;

    public Album(String name, String artistId, int releaseYear) {
        this.name = name;
        this.artistId = artistId;
        this.releaseYear = releaseYear;
        this.id = "album-" + UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public String getArtistId() {
        return artistId;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getId() {
        return id;
    }
}
