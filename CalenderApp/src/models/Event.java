package models;

import enums.EventStatus;
import enums.RecurrenceType;

import java.time.LocalDateTime;
import java.util.*;

public abstract class Event implements Comparable<Event> {
    protected String eventId;
    protected String title;
    protected String description;
    protected String location;
    protected TimeSlot timeSlot;
    protected List<User> participants;
    protected EventStatus status;
    protected RecurrenceType recurrenceType;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public Event(String title, String description, String location, 
                 TimeSlot timeSlot, List<User> participants, RecurrenceType recurrenceType) {
        this.eventId = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.location = location;
        this.timeSlot = timeSlot;
        this.participants = new ArrayList<>(participants);
        this.status = EventStatus.SCHEDULED;
        this.recurrenceType = recurrenceType;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Abstract method to get next occurrence of event
    public abstract Event getNextEvent(LocalDateTime afterDate);

    // Getters
    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public List<User> getParticipants() {
        return new ArrayList<>(participants);
    }

    public EventStatus getStatus() {
        return status;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setLocation(String location) {
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatus(EventStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void addParticipant(User user) {
        if (!participants.contains(user)) {
            participants.add(user);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void removeParticipant(User user) {
        participants.remove(user);
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", timeSlot=" + timeSlot +
                ", participants=" + participants.size() +
                ", status=" + status +
                ", recurrenceType=" + recurrenceType +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event event = (Event) obj;
        return eventId.equals(event.eventId);
    }

    @Override
    public int hashCode() {
        return eventId.hashCode();
    }

    @Override
    public int compareTo(Event other) {
        // Sort by start time, then by end time, then by eventId for uniqueness
        int startCompare = this.timeSlot.getStartTime().compareTo(other.timeSlot.getStartTime());
        if (startCompare != 0) return startCompare;
        
        int endCompare = this.timeSlot.getEndTime().compareTo(other.timeSlot.getEndTime());
        if (endCompare != 0) return endCompare;
        
        return this.eventId.compareTo(other.eventId);
    }
}
