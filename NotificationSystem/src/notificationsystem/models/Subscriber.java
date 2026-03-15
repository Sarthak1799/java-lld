package notificationsystem.models;
import java.util.*;

public interface Subscriber {
    public void sendNotification(List<String> userIds, Notification notification);
}
