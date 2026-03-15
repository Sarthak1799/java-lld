package observers;

import enums.NotificationType;
import models.Event;

public interface EventObserver {
    void notify(Event event, NotificationType notificationType, String message);
}
