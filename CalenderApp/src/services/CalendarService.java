package services;

import enums.EventStatus;
import enums.NotificationType;
import models.*;
import observers.EventObserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CalendarService {
    private static CalendarService instance;
    private List<EventObserver> observers;
    private LocalDateTime systemDate;

    private CalendarService() {
        this.observers = new ArrayList<>();
        this.systemDate = LocalDateTime.now();
    }

    public static CalendarService getInstance() {
        if (instance == null) {
            instance = new CalendarService();
        }
        return instance;
    }

    // Observer pattern - register observers
    public void registerObserver(EventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(EventObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Event event, NotificationType type, String message) {
        for (EventObserver observer : observers) {
            observer.notify(event, type, message);
        }
    }

    // Get system date
    public LocalDateTime getSystemDate() {
        return systemDate;
    }

    // Create event - checks availability and adds to all participants' calendars
    public Event createEvent(String title, String description, String location, 
                            TimeSlot timeSlot, List<User> participants, String eventType) {
        
        // Check if all participants are available
        for (User user : participants) {
            if (!user.getCalendar().isAvailable(timeSlot)) {
                System.out.println("User " + user.getName() + " is not available during this time slot");
                return null;
            }
        }

        // Create appropriate event type
        Event event;
        switch (eventType.toUpperCase()) {
            case "DAILY":
                event = new DailyEvent(title, description, location, timeSlot, participants);
                break;
            case "WEEKLY":
                event = new WeeklyEvent(title, description, location, timeSlot, participants);
                break;
            case "MONTHLY":
                event = new MonthlyEvent(title, description, location, timeSlot, participants);
                break;
            default:
                event = new OneTimeEvent(title, description, location, timeSlot, participants);
                break;
        }

        // Add event to all participants' calendars
        for (User user : participants) {
            user.getCalendar().addEvent(event);
        }

        // Notify observers
        notifyObservers(event, NotificationType.EVENT_CREATED, 
                       "New event created: " + title);

        System.out.println("Event created successfully: " + event.getTitle());
        return event;
    }

    // Update event
    public void updateEvent(Event event, String newTitle, String newDescription, 
                          String newLocation, TimeSlot newTimeSlot) {
        
        // If time slot is changing, check availability
        if (newTimeSlot != null && !newTimeSlot.equals(event.getTimeSlot())) {
            for (User user : event.getParticipants()) {
                models.Calendar calendar = user.getCalendar();
                // Temporarily remove the event to check availability
                calendar.removeEvent(event.getEventId());
                boolean isAvailable = calendar.isAvailable(newTimeSlot);
                calendar.addEvent(event); // Add it back
                
                if (!isAvailable) {
                    System.out.println("Cannot update: User " + user.getName() + 
                                     " is not available during the new time slot");
                    return;
                }
            }
            event.setTimeSlot(newTimeSlot);
        }

        if (newTitle != null) {
            event.setTitle(newTitle);
        }
        if (newDescription != null) {
            event.setDescription(newDescription);
        }
        if (newLocation != null) {
            event.setLocation(newLocation);
        }

        // Notify observers
        notifyObservers(event, NotificationType.EVENT_UPDATED, 
                       "Event updated: " + event.getTitle());

        System.out.println("Event updated successfully: " + event.getTitle());
    }

    // Delete event from all participants' calendars
    public void deleteEvent(Event event) {
        for (User user : event.getParticipants()) {
            user.getCalendar().removeEvent(event.getEventId());
        }

        event.setStatus(EventStatus.CANCELLED);

        // Notify observers
        notifyObservers(event, NotificationType.EVENT_DELETED, 
                       "Event cancelled: " + event.getTitle());

        System.out.println("Event deleted successfully: " + event.getTitle());
    }

    // Get common available time slots for multiple users
    public List<TimeSlot> getCommonAvailableSlots(List<User> users, LocalDate date, 
                                                   int slotDurationMinutes) {
        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        // Collect all busy slots from all users
        List<TimeSlot> allBusySlots = new ArrayList<>();
        for (User user : users) {
            List<Event> userEvents = user.getCalendar().getEventsForDate(date);
            for (Event event : userEvents) {
                allBusySlots.add(event.getTimeSlot());
            }
        }

        // Sort busy slots by start time
        allBusySlots.sort((ts1, ts2) -> ts1.getStartTime().compareTo(ts2.getStartTime()));

        // Merge overlapping busy slots
        List<TimeSlot> mergedBusySlots = mergeBusySlots(allBusySlots);

        // Find gaps (available slots) in merged busy slots
        return findAvailableSlots(mergedBusySlots, date);
    }

    // Merge overlapping busy slots - O(n)
    private List<TimeSlot> mergeBusySlots(List<TimeSlot> busySlots) {
        if (busySlots.isEmpty()) {
            return new ArrayList<>();
        }

        List<TimeSlot> merged = new ArrayList<>();
        TimeSlot current = busySlots.get(0);

        for (int i = 1; i < busySlots.size(); i++) {
            TimeSlot next = busySlots.get(i);
            
            if (current.overlaps(next) || current.getEndTime().equals(next.getStartTime())) {
                // Merge overlapping or adjacent slots
                LocalDateTime newEnd = current.getEndTime().isAfter(next.getEndTime()) 
                                     ? current.getEndTime() : next.getEndTime();
                current = new TimeSlot(current.getStartTime(), newEnd);
            } else {
                // No overlap, add current and move to next
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);

        return merged;
    }

    // Find available slots from gaps in busy slots - O(n)
    private List<TimeSlot> findAvailableSlots(List<TimeSlot> mergedBusySlots, LocalDate date) {
        List<TimeSlot> availableSlots = new ArrayList<>();
        LocalDateTime dayStart = date.atTime(9, 0); // 9 AM
        LocalDateTime dayEnd = date.atTime(18, 0);  // 6 PM
        
        LocalDateTime currentTime = dayStart;

        for (TimeSlot busySlot : mergedBusySlots) {
            // Add gap before this busy slot
            if (currentTime.isBefore(busySlot.getStartTime())) {
                availableSlots.add(new TimeSlot(currentTime, busySlot.getStartTime()));
            }
            // Move current time to end of busy slot
            if (currentTime.isBefore(busySlot.getEndTime())) {
                currentTime = busySlot.getEndTime();
            }
        }

        // Add remaining time after last busy slot
        if (currentTime.isBefore(dayEnd)) {
            availableSlots.add(new TimeSlot(currentTime, dayEnd));
        }

        return availableSlots;
    }

    // Advance system time and process completed events
    public void step(LocalDateTime newDate) {
        System.out.println("\n=== ADVANCING TIME ===");
        System.out.println("From: " + systemDate);
        System.out.println("To: " + newDate);
        
        if (newDate.isBefore(systemDate)) {
            System.out.println("Error: Cannot go back in time!");
            return;
        }

        UserService userService = UserService.getInstance();
        Map<String, User> allUsers = userService.getAllUsers();
        
        List<Event> processedEvents = new ArrayList<>();
        
        // Process all users' calendars
        for (User user : allUsers.values()) {
            models.Calendar calendar = user.getCalendar();
            Map<String, Event> events = calendar.getEvents();
            
            for (Event event : new ArrayList<>(events.values())) {
                // Skip if already processed (shared events)
                if (processedEvents.contains(event)) {
                    continue;
                }
                
                // Check if event is completed
                if (event.getTimeSlot().isBeforeDate(newDate) && 
                    event.getStatus() == EventStatus.SCHEDULED) {
                    
                    // Mark as completed
                    event.setStatus(EventStatus.COMPLETED);
                    processedEvents.add(event);
                    
                    System.out.println("Completed event: " + event.getTitle() + 
                                     " (" + event.getTimeSlot() + ")");
                    
                    // Notify observers
                    notifyObservers(event, NotificationType.EVENT_COMPLETED, 
                                  "Event completed: " + event.getTitle());
                    
                    // Get next occurrence for recurring events
                    Event nextEvent = event.getNextEvent(newDate);
                    if (nextEvent != null) {
                        System.out.println("Creating next occurrence: " + nextEvent.getTitle() + 
                                         " (" + nextEvent.getTimeSlot() + ")");
                        
                        // Add next event to all participants' calendars
                        for (User participant : nextEvent.getParticipants()) {
                            participant.getCalendar().addEvent(nextEvent);
                        }
                        
                        // Notify observers about the new occurrence
                        notifyObservers(nextEvent, NotificationType.EVENT_CREATED, 
                                      "Next occurrence created: " + nextEvent.getTitle());
                    }
                }
            }
        }
        
        systemDate = newDate;
        System.out.println("=== TIME ADVANCED ===\n");
    }

    // Get all events for a user on a specific date
    public List<Event> getEventsForUser(User user, LocalDate date) {
        return user.getCalendar().getEventsForDate(date);
    }
}
