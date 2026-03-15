package services;

import models.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private Map<String, User> users;
    private static UserService instance;

    private UserService() {
        this.users = new HashMap<>();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }

    public boolean userExists(String userId) {
        return users.containsKey(userId);
    }
}
