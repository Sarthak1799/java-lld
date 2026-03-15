package models;

import java.util.*;

public class Song {

    private String title;
    private String artistId;
    private String albumId;
    private int duration; // duration in seconds
    private String id;

    public Song(String title, String artistId, String albumId, int duration) {
        this.title = title;
        this.artistId = artistId;
        this.albumId = albumId;
        this.duration = duration;
        this.id = "song-" + UUID.randomUUID().toString();
    }

    public String getTitle() {
        return title;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public int getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }
}
