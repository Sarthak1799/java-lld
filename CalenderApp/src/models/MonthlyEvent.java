package models;

import enums.RecurrenceType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MonthlyEvent extends Event {

    public MonthlyEvent(String title, String description, String location, 
                        TimeSlot timeSlot, List<User> participants) {
        super(title, description, location, timeSlot, participants, RecurrenceType.MONTHLY);
    }

    @Override
    public Event getNextEvent(LocalDateTime afterDate) {
        // Calculate the next occurrence after the given date
        LocalDateTime currentStart = this.timeSlot.getStartTime();
        LocalDateTime currentEnd = this.timeSlot.getEndTime();
        
        // Find the next month after the given date
        long monthsToAdd = ChronoUnit.MONTHS.between(currentStart, afterDate) + 1;
        if (monthsToAdd < 1) {
            monthsToAdd = 1;
        }
        
        LocalDateTime nextStart = currentStart.plusMonths(monthsToAdd);
        LocalDateTime nextEnd = currentEnd.plusMonths(monthsToAdd);
        
        TimeSlot nextTimeSlot = new TimeSlot(nextStart, nextEnd);
        
        return new MonthlyEvent(this.title, this.description, this.location, 
                               nextTimeSlot, this.participants);
    }
}
