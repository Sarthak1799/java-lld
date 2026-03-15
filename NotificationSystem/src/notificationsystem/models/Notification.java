package notificationsystem.models;
import java.util.*;

public abstract class Notification {
    private String id;
    private EventType eventType;
    private NotificationType type;
    private String data;

    public Notification(EventType eventType, NotificationType type, String data){
        this.data=  data;
        this.eventType = eventType;
        this.type = type;
        this.id = "notification-" + UUID.randomUUID().toString();
    }

    public NotificationType getType() {
        return type;
    }

    public EventType getEventType(){
        return eventType;
    }

    public String getData() {
        return data;
    }
}
