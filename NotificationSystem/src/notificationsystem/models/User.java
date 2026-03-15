package notificationsystem.models;
import java.util.*;

public class User {
    private String userId;
    private String name;
    private String username;

    public User(String name, String username){
        this.name = name;
        this.username = username;
        this.userId= "user-" + UUID.randomUUID().toString();
    }

    public String getId() {
        return userId;
    }

    public void displayNotification(Notification notification){
        System.out.println("User " + name + " received notification of type " + notification.getType() + " for event " + notification.getEventType() + " with data: " + notification.getData());
    }
}
