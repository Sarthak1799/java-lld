package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        this.endTime = endTime;
    }

    // Check if this timeslot overlaps with another
    public boolean overlaps(TimeSlot other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    // Check if a given time is within this timeslot
    public boolean contains(LocalDateTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    // Check if the timeslot has passed a given date
    public boolean isBeforeDate(LocalDateTime date) {
        return this.endTime.isBefore(date) || this.endTime.isEqual(date);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return startTime.format(formatter) + " to " + endTime.format(formatter);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) obj;
        return startTime.equals(timeSlot.startTime) && endTime.equals(timeSlot.endTime);
    }

    @Override
    public int hashCode() {
        return startTime.hashCode() + endTime.hashCode();
    }
}
