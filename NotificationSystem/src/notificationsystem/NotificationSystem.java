package notificationsystem;
import notificationsystem.models.*;
import java.util.*;

public class NotificationSystem {
    private NotificationService notificationService;
    private UserService userService;
    private Map<EventType, List<String>> eventSubscriptions;
    
    public NotificationSystem(){
        this.notificationService = NotificationService.getInstance();
        this.userService = UserService.getInstance();
        this.eventSubscriptions = new HashMap<>();
        
        // Register UserService as a subscriber
        notificationService.registerSubscriber(userService);
    }
    
    public String registerUser(String name, String username){
        return userService.registerUser(name, username);
    }
    
    public void subscribeToEvent(String userId, EventType eventType){
        eventSubscriptions.putIfAbsent(eventType, new ArrayList<>());
        List<String> subscribers = eventSubscriptions.get(eventType);
        if(!subscribers.contains(userId)){
            subscribers.add(userId);
        }
        System.out.println("User " + userId + " subscribed to " + eventType);
    }
    
    public void unsubscribeFromEvent(String userId, EventType eventType){
        if(eventSubscriptions.containsKey(eventType)){
            eventSubscriptions.get(eventType).remove(userId);
            System.out.println("User " + userId + " unsubscribed from " + eventType);
        }
    }
    
    public void pushNotification(Notification notification){
        EventType eventType = notification.getEventType();
        List<String> subscribedUsers = eventSubscriptions.getOrDefault(eventType, new ArrayList<>());
        
        if(subscribedUsers.isEmpty()){
            System.out.println("No subscribers for event: " + eventType);
            return;
        }
        
        notificationService.pushNotification(notification, subscribedUsers);
    }
    
    public User getUser(String userId){
        return userService.getUser(userId);
    }
}
