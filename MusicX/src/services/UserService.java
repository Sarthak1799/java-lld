package services;

import java.util.*;
import models.*;

public class UserService {

    private static UserService instance = null;
    private Map<String, User> users;
    private Map<String, UserSession> sessions;

    public UserService() {
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public String registerUser(String username, String email, String passwordHash) {
        User user = new User(username, email, passwordHash);
        users.put(user.getId(), user);
        return user.getId();
    }

    public void login(String userId, String passwordHash) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }

        User user = users.get(userId);
        if (!user.getPasswordHash().equals(passwordHash)) {
            throw new IllegalArgumentException("Invalid password");
        }

        UserSession session = new UserSession(userId);
        sessions.put(session.getUserId(), session);
    }

    public MusicPlayer getMusicPlayer(String userId) {
        if (!sessions.containsKey(userId)) {
            throw new IllegalArgumentException("Invalid session");
        }

        UserSession session = sessions.get(userId);
        return session.getMusicPlayer();
    }

    public void logout(String userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }

        sessions.remove(userId);
    }

    public List<Song> getUserSongLog(String userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        return users.get(userId).getSongLog();
    }

    public void registerSongForUser(String userId, Song song) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        users.get(userId).logSong(song);
    }
}
