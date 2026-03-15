package models;

import enums.RecurrenceType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DailyEvent extends Event {

    public DailyEvent(String title, String description, String location, 
                      TimeSlot timeSlot, List<User> participants) {
        super(title, description, location, timeSlot, participants, RecurrenceType.DAILY);
    }

    @Override
    public Event getNextEvent(LocalDateTime afterDate) {
        // Calculate the next occurrence after the given date
        LocalDateTime currentStart = this.timeSlot.getStartTime();
        LocalDateTime currentEnd = this.timeSlot.getEndTime();
        
        // Find the next day after the given date
        long daysToAdd = ChronoUnit.DAYS.between(currentStart, afterDate) + 1;
        if (daysToAdd < 1) {
            daysToAdd = 1;
        }
        
        LocalDateTime nextStart = currentStart.plusDays(daysToAdd);
        LocalDateTime nextEnd = currentEnd.plusDays(daysToAdd);
        
        TimeSlot nextTimeSlot = new TimeSlot(nextStart, nextEnd);
        
        return new DailyEvent(this.title, this.description, this.location, 
                             nextTimeSlot, this.participants);
    }
}
