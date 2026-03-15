package notificationsystem.models;

public class SMSNotification extends Notification {
    public SMSNotification(String data, EventType eventType){
        super(eventType, NotificationType.SMS, data);
    }

    
}
