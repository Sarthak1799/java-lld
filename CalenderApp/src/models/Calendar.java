package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Calendar {
    private String calendarId;
    private User owner;
    private Map<LocalDate, TreeSet<Event>> eventsByDate; // date -> sorted events
    private Map<String, Event> eventsById; // eventId -> Event for quick lookup

    public Calendar(User owner) {
        this.calendarId = UUID.randomUUID().toString();
        this.owner = owner;
        this.eventsByDate = new HashMap<>();
        this.eventsById = new HashMap<>();
    }

    public String getCalendarId() {
        return calendarId;
    }

    public User getOwner() {
        return owner;
    }

    public Map<String, Event> getEvents() {
        return new HashMap<>(eventsById);
    }

    // Add event to calendar
    public void addEvent(Event event) {
        eventsById.put(event.getEventId(), event);
        
        // Add to date-based index
        LocalDate eventDate = event.getTimeSlot().getStartTime().toLocalDate();
        eventsByDate.computeIfAbsent(eventDate, k -> new TreeSet<>()).add(event);
    }

    // Remove event from calendar
    public void removeEvent(String eventId) {
        Event event = eventsById.remove(eventId);
        if (event != null) {
            LocalDate eventDate = event.getTimeSlot().getStartTime().toLocalDate();
            TreeSet<Event> dateEvents = eventsByDate.get(eventDate);
            if (dateEvents != null) {
                dateEvents.remove(event);
                if (dateEvents.isEmpty()) {
                    eventsByDate.remove(eventDate);
                }
            }
        }
    }

    // Get event by ID
    public Event getEvent(String eventId) {
        return eventsById.get(eventId);
    }

    // Get all events for a specific date - O(1) lookup instead of O(n) scan
    public List<Event> getEventsForDate(LocalDate date) {
        TreeSet<Event> dateEvents = eventsByDate.get(date);
        if (dateEvents == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(dateEvents);
    }

    // Get available time slots for a specific date
    public List<TimeSlot> getAvailableSlots(LocalDate date, int slotDurationMinutes) {
        LocalDateTime dayStart = date.atTime(9, 0); // 9 AM
        LocalDateTime dayEnd = date.atTime(18, 0);  // 6 PM
        
        // Events are already sorted by start time in TreeSet
        List<TimeSlot> busySlots = getEventsForDate(date).stream()
                .map(Event::getTimeSlot)
                .collect(Collectors.toList());
        
        List<TimeSlot> availableSlots = new ArrayList<>();
        LocalDateTime currentTime = dayStart;
        
        for (TimeSlot busySlot : busySlots) {
            if (currentTime.isBefore(busySlot.getStartTime())) {
                availableSlots.add(new TimeSlot(currentTime, busySlot.getStartTime()));
            }
            if (currentTime.isBefore(busySlot.getEndTime())) {
                currentTime = busySlot.getEndTime();
            }
        }
        
        if (currentTime.isBefore(dayEnd)) {
            availableSlots.add(new TimeSlot(currentTime, dayEnd));
        }
        
        return availableSlots;
    }

    // Check if user is available during a timeslot
    public boolean isAvailable(TimeSlot timeSlot) {
        LocalDate date = timeSlot.getStartTime().toLocalDate();
        TreeSet<Event> dateEvents = eventsByDate.get(date);
        
        if (dateEvents == null) {
            return true;
        }
        
        for (Event event : dateEvents) {
            if (event.getTimeSlot().overlaps(timeSlot)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "calendarId='" + calendarId + '\'' +
                ", owner=" + owner.getName() +
                ", eventsCount=" + eventsById.size() +
                '}';
    }
}
