package services;

import java.util.*;
import models.*;

public class MusicXSystem {

    private static MusicXSystem instance = null;
    private UserService userService;
    private MusicService musicService;

    private MusicXSystem() {
        this.userService = UserService.getInstance();
        this.musicService = MusicService.getInstance();
    }

    public static MusicXSystem getInstance() {
        if (instance == null) {
            instance = new MusicXSystem();
        }
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public String registerUser(String username, String email, String passwordHash) {
        return userService.registerUser(username, email, passwordHash);
    }

    public void login(String userId, String passwordHash) {
        userService.login(userId, passwordHash);
    }

    public MusicPlayer getMusicPlayer(String userId) {
        return userService.getMusicPlayer(userId);
    }

    public void logout(String userId) {
        userService.logout(userId);
    }

    public Song findSongByName(String name) {
        return musicService.findSongByName(name);
    }

    public List<Song> getSongsByArtist(String artistId) {
        return musicService.getSongsByArtist(artistId);
    }

    public List<Song> getSongsByAlbum(String albumId) {
        return musicService.getSongsByAlbum(albumId);
    }

    public List<Album> getAlbumsByArtist(String artistId) {
        return musicService.getAlbumsByArtist(artistId);
    }

    public Song recommendSong(String userId, SongRecommendation strategy) {
        return musicService.recommendSong(userId, strategy);
    }

    public void registerSongForUser(String userId, Song song) {
        userService.registerSongForUser(userId, song);
    }

}
