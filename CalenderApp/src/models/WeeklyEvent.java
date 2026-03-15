package models;

import enums.RecurrenceType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class WeeklyEvent extends Event {

    public WeeklyEvent(String title, String description, String location, 
                       TimeSlot timeSlot, List<User> participants) {
        super(title, description, location, timeSlot, participants, RecurrenceType.WEEKLY);
    }

    @Override
    public Event getNextEvent(LocalDateTime afterDate) {
        // Calculate the next occurrence after the given date
        LocalDateTime currentStart = this.timeSlot.getStartTime();
        LocalDateTime currentEnd = this.timeSlot.getEndTime();
        
        // Find the next week after the given date
        long weeksToAdd = ChronoUnit.WEEKS.between(currentStart, afterDate) + 1;
        if (weeksToAdd < 1) {
            weeksToAdd = 1;
        }
        
        LocalDateTime nextStart = currentStart.plusWeeks(weeksToAdd);
        LocalDateTime nextEnd = currentEnd.plusWeeks(weeksToAdd);
        
        TimeSlot nextTimeSlot = new TimeSlot(nextStart, nextEnd);
        
        return new WeeklyEvent(this.title, this.description, this.location, 
                              nextTimeSlot, this.participants);
    }
}
