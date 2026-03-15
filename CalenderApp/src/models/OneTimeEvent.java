package models;

import enums.RecurrenceType;

import java.time.LocalDateTime;
import java.util.List;

public class OneTimeEvent extends Event {

    public OneTimeEvent(String title, String description, String location, 
                        TimeSlot timeSlot, List<User> participants) {
        super(title, description, location, timeSlot, participants, RecurrenceType.NONE);
    }

    @Override
    public Event getNextEvent(LocalDateTime afterDate) {
        // One-time events don't repeat
        return null;
    }
}
