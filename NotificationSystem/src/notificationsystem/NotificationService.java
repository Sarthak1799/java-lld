package notificationsystem;
import notificationsystem.models.*;
import java.util.*;

public class NotificationService {
    private static NotificationService instance = null;
    private List<Subscriber> subscribers;
    
    public NotificationService() {
        this.subscribers = new ArrayList<>();
    }

    public static NotificationService getInstance() {
        if(instance == null)
            instance = new NotificationService();

        return instance;
    }
    
    public void registerSubscriber(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public void pushNotification(Notification notification, List<String> userIds){
        for(Subscriber sub : subscribers){
            sub.sendNotification(userIds, notification);
        }
    }
    
}
