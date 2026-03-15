import notificationsystem.*;
import notificationsystem.models.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== Notification System Demo =====");
        
        // Create notification system
        NotificationSystem notificationSystem = new NotificationSystem();
        
        // Step 1: Register users and get their IDs
        System.out.println("\n1. Registering users...");
        String aliceId = notificationSystem.registerUser("Alice", "alice123");
        String bobId = notificationSystem.registerUser("Bob", "bob456");
        String charlieId = notificationSystem.registerUser("Charlie", "charlie789");
        System.out.println("All users registered successfully!");
        System.out.println("Alice ID: " + aliceId);
        System.out.println("Bob ID: " + bobId);
        System.out.println("Charlie ID: " + charlieId);
        
        // Step 2: Subscribe users to different events
        System.out.println("\n2. Setting up subscriptions...");
        System.out.println("Alice subscribes to NEWS and GENERAL");
        notificationSystem.subscribeToEvent(aliceId, EventType.NEWS);
        notificationSystem.subscribeToEvent(aliceId, EventType.GENERAL);
        
        System.out.println("Bob subscribes to PARTY and GENERAL");
        notificationSystem.subscribeToEvent(bobId, EventType.PARTY);
        notificationSystem.subscribeToEvent(bobId, EventType.GENERAL);
        
        System.out.println("Charlie subscribes to NEWS and PARTY");
        notificationSystem.subscribeToEvent(charlieId, EventType.NEWS);
        notificationSystem.subscribeToEvent(charlieId, EventType.PARTY);
        
        // Step 3: Push notifications
        System.out.println("\n3. Pushing notifications...");
        
        // Create NEWS notification
        System.out.println("\n--- Sending NEWS notification (Alice and Charlie should receive) ---");
        Notification newsNotification = new SMSNotification(
            "Breaking: New Java 21 features released!", 
            EventType.NEWS
        );
        notificationSystem.pushNotification(newsNotification);
        
        // Create PARTY notification
        System.out.println("\n--- Sending PARTY notification (Bob and Charlie should receive) ---");
        Notification partyNotification = new SMSNotification(
            "You're invited to the team celebration party!", 
            EventType.PARTY
        );
        notificationSystem.pushNotification(partyNotification);
        
        // Create GENERAL notification
        System.out.println("\n--- Sending GENERAL notification (Alice and Bob should receive) ---");
        Notification generalNotification = new SMSNotification(
            "System maintenance scheduled for tonight", 
            EventType.GENERAL
        );
        notificationSystem.pushNotification(generalNotification);
        
        // Step 4: Unsubscribe a user
        System.out.println("\n4. Unsubscribing Alice from NEWS...");
        notificationSystem.unsubscribeFromEvent(aliceId, EventType.NEWS);
        
        // Step 5: Send another NEWS notification
        System.out.println("\n5. Sending another NEWS notification (only Charlie should receive) ---");
        Notification newsNotification2 = new SMSNotification(
            "Update: Additional Java 21 documentation available!", 
            EventType.NEWS
        );
        notificationSystem.pushNotification(newsNotification2);
        
        System.out.println("\n===== Demo Complete =====");
    }
}