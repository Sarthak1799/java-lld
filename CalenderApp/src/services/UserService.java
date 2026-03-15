package services;

import enums.NotificationType;
import models.Event;
import models.User;
import observers.EventObserver;

import java.util.HashMap;
import java.util.Map;

public class UserService implements EventObserver {
    private static UserService instance;
    private Map<String, User> users; // userId -> User

    private UserService() {
        this.users = new HashMap<>();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    // Create a new user
    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getUserId(), user);
        System.out.println("Created user: " + user.getName() + " (" + user.getEmail() + ")");
        return user;
    }

    // Get user by ID
    public User getUser(String userId) {
        return users.get(userId);
    }

    // Get all users
    public Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }

    // Delete user
    public void deleteUser(String userId) {
        User user = users.remove(userId);
        if (user != null) {
            System.out.println("Deleted user: " + user.getName());
        }
    }

    // Observer pattern implementation - receive notifications about events
    @Override
    public void notify(Event event, NotificationType notificationType, String message) {
        // Notify all participants of the event
        for (User participant : event.getParticipants()) {
            System.out.println("\n=== NOTIFICATION ===");
            System.out.println("To: " + participant.getName() + " (" + participant.getEmail() + ")");
            System.out.println("Type: " + notificationType);
            System.out.println("Event: " + event.getTitle());
            System.out.println("Time: " + event.getTimeSlot());
            System.out.println("Location: " + event.getLocation());
            System.out.println("Message: " + message);
            System.out.println("==================\n");
        }
    }
}
