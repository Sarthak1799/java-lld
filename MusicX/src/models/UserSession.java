package models;

import java.util.*;

public class UserSession {

    private String userId;
    private String sessionId;
    private MusicPlayer musicPlayer;

    public UserSession(String userId) {
        this.userId = userId;
        this.sessionId = "session-" + UUID.randomUUID().toString();
        this.musicPlayer = new MusicPlayer(userId);
    }

    public MusicPlayer getMusicPlayer() {
        return this.musicPlayer;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
