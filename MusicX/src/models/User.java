package models;

import java.util.*;

public class User {

    private String username;
    private String email;
    private String passwordHash;
    private String id;
    private List<Song> songLog;

    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.id = "user-" + UUID.randomUUID().toString();
        this.songLog = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getId() {
        return id;
    }

    public void logSong(Song song) {
        this.songLog.add(song);
    }

    public List<Song> getSongLog() {
        return songLog;
    }
}
