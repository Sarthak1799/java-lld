package services;

import java.util.*;
import models.*;

public class MusicService {

    private Map<String, Artist> artists;
    private Map<String, Song> songs;
    private Map<String, Album> albums;
    private static MusicService instance = null;

    public MusicService() {
        this.artists = new HashMap<>();
        this.songs = new HashMap<>();
        this.albums = new HashMap<>();
    }

    private void initializeSampleData() {
        Artist artist1 = new Artist("The Beatles", "Rock");
        Artist artist2 = new Artist("Adele", "Pop");
        artists.put(artist1.getId(), artist1);
        artists.put(artist2.getId(), artist2);

        Album album1 = new Album("Abbey Road", artist1.getId(), 1969);
        Album album2 = new Album("25", artist2.getId(), 2015);
        albums.put(album1.getId(), album1);
        albums.put(album2.getId(), album2);

        Song song1 = new Song("Come Together", artist1.getId(), album1.getId(), 259);
        Song song2 = new Song("Hello", artist2.getId(), album2.getId(), 295);
        songs.put(song1.getId(), song1);
        songs.put(song2.getId(), song2);
    }

    public static MusicService getInstance() {
        if (instance == null) {
            instance = new MusicService();
            instance.initializeSampleData();
        }
        return instance;
    }

    public Song findSongByName(String name) {
        for (Song song : songs.values()) {
            if (song.getTitle().equalsIgnoreCase(name)) {
                return song;
            }
        }
        return null;
    }

    public List<Song> getSongsByArtist(String artistId) {
        List<Song> result = new ArrayList<>();
        for (Song song : songs.values()) {
            if (song.getArtistId().equals(artistId)) {
                result.add(song);
            }
        }
        return result;
    }

    public List<Song> getSongsByAlbum(String albumId) {
        List<Song> result = new ArrayList<>();
        for (Song song : songs.values()) {
            if (song.getAlbumId().equals(albumId)) {
                result.add(song);
            }
        }
        return result;
    }

    public List<Album> getAlbumsByArtist(String artistId) {
        List<Album> result = new ArrayList<>();
        for (Album album : albums.values()) {
            if (album.getArtistId().equals(artistId)) {
                result.add(album);
            }
        }
        return result;
    }

    public Song recommendSong(String userId, SongRecommendation strategy) {
        UserService userService = UserService.getInstance();
        List<Song> songLog = userService.getUserSongLog(userId);
        return strategy.recommendSong(userId, songLog);
    }

}
