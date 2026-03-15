import models.*;
import services.CalendarService;
import services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("      CALENDAR APP - LOW LEVEL DESIGN");
        System.out.println("==============================================\n");

        // Initialize services
        UserService userService = UserService.getInstance();
        CalendarService calendarService = CalendarService.getInstance();
        
        // Register UserService as observer
        calendarService.registerObserver(userService);

        // Create users
        System.out.println("--- Creating Users ---");
        User alice = userService.createUser("Alice Johnson", "alice@example.com");
        User bob = userService.createUser("Bob Smith", "bob@example.com");
        User charlie = userService.createUser("Charlie Brown", "charlie@example.com");
        System.out.println();

        // Scenario 1: Create a one-time event
        System.out.println("--- Scenario 1: Creating One-Time Event ---");
        LocalDateTime meetingStart = LocalDateTime.of(2026, 1, 28, 10, 0);
        LocalDateTime meetingEnd = LocalDateTime.of(2026, 1, 28, 11, 0);
        TimeSlot meetingSlot = new TimeSlot(meetingStart, meetingEnd);
        
        Event teamMeeting = calendarService.createEvent(
            "Team Standup",
            "Daily team standup meeting",
            "Conference Room A",
            meetingSlot,
            Arrays.asList(alice, bob, charlie),
            "ONETIME"
        );
        System.out.println();

        // Scenario 2: Check available slots for multiple users
        System.out.println("--- Scenario 2: Finding Common Available Slots ---");
        LocalDate targetDate = LocalDate.of(2026, 1, 28);
        List<TimeSlot> commonSlots = calendarService.getCommonAvailableSlots(
            Arrays.asList(alice, bob), 
            targetDate, 
            60
        );
        System.out.println("Common available slots for Alice and Bob on " + targetDate + ":");
        for (TimeSlot slot : commonSlots) {
            System.out.println("  - " + slot);
        }
        System.out.println();

        // Scenario 3: Create a recurring weekly event
        System.out.println("--- Scenario 3: Creating Weekly Recurring Event ---");
        LocalDateTime weeklyStart = LocalDateTime.of(2026, 1, 29, 14, 0);
        LocalDateTime weeklyEnd = LocalDateTime.of(2026, 1, 29, 15, 0);
        TimeSlot weeklySlot = new TimeSlot(weeklyStart, weeklyEnd);
        
        Event weeklyReview = calendarService.createEvent(
            "Weekly Review",
            "Weekly project review meeting",
            "Virtual - Zoom",
            weeklySlot,
            Arrays.asList(alice, bob),
            "WEEKLY"
        );
        System.out.println();

        // Scenario 4: Update an event
        System.out.println("--- Scenario 4: Updating Event ---");
        LocalDateTime newStart = LocalDateTime.of(2026, 1, 28, 11, 0);
        LocalDateTime newEnd = LocalDateTime.of(2026, 1, 28, 12, 0);
        TimeSlot newSlot = new TimeSlot(newStart, newEnd);
        
        calendarService.updateEvent(
            teamMeeting,
            "Team Standup - Updated",
            null,
            "Conference Room B",
            newSlot
        );
        System.out.println();

        // Scenario 5: View events for a user
        System.out.println("--- Scenario 5: Viewing Alice's Events ---");
        List<Event> aliceEvents = calendarService.getEventsForUser(alice, targetDate);
        System.out.println("Alice's events on " + targetDate + ":");
        for (Event event : aliceEvents) {
            System.out.println("  - " + event.getTitle() + " at " + event.getTimeSlot());
        }
        System.out.println();

        // Scenario 6: Create daily recurring event
        System.out.println("--- Scenario 6: Creating Daily Recurring Event ---");
        LocalDateTime dailyStart = LocalDateTime.of(2026, 1, 28, 9, 0);
        LocalDateTime dailyEnd = LocalDateTime.of(2026, 1, 28, 9, 30);
        TimeSlot dailySlot = new TimeSlot(dailyStart, dailyEnd);
        
        Event morningStandup = calendarService.createEvent(
            "Morning Standup",
            "Quick daily sync",
            "Virtual",
            dailySlot,
            Arrays.asList(alice, bob, charlie),
            "DAILY"
        );
        System.out.println();

        // Scenario 7: Advance time and process completed events
        System.out.println("--- Scenario 7: Advancing Time (Step Function) ---");
        LocalDateTime futureDate = LocalDateTime.of(2026, 1, 29, 18, 0);
        calendarService.step(futureDate);
        System.out.println();

        // Scenario 8: View events after time advancement
        System.out.println("--- Scenario 8: Viewing Events After Time Advancement ---");
        LocalDate nextDay = LocalDate.of(2026, 1, 29);
        List<Event> aliceNextDayEvents = calendarService.getEventsForUser(alice, nextDay);
        System.out.println("Alice's events on " + nextDay + ":");
        for (Event event : aliceNextDayEvents) {
            System.out.println("  - " + event.getTitle() + " at " + event.getTimeSlot() + 
                             " [Status: " + event.getStatus() + "]");
        }
        System.out.println();

        // Scenario 9: Try to create conflicting event
        System.out.println("--- Scenario 9: Attempting to Create Conflicting Event ---");
        LocalDateTime conflictStart = LocalDateTime.of(2026, 1, 29, 14, 15);
        LocalDateTime conflictEnd = LocalDateTime.of(2026, 1, 29, 14, 45);
        TimeSlot conflictSlot = new TimeSlot(conflictStart, conflictEnd);
        
        Event conflictEvent = calendarService.createEvent(
            "Another Meeting",
            "This should fail",
            "Room X",
            conflictSlot,
            Arrays.asList(alice, bob),
            "ONETIME"
        );
        if (conflictEvent == null) {
            System.out.println("Event creation failed due to conflict (as expected)");
        }
        System.out.println();

        // Scenario 10: Delete an event
        System.out.println("--- Scenario 10: Deleting Event ---");
        calendarService.deleteEvent(teamMeeting);
        System.out.println();

        // Scenario 11: Advance time further to see weekly recurrence
        System.out.println("--- Scenario 11: Advancing Time by One Week ---");
        LocalDateTime nextWeek = LocalDateTime.of(2026, 2, 5, 18, 0);
        calendarService.step(nextWeek);
        System.out.println();

        // View events after week advancement
        LocalDate nextWeekDate = LocalDate.of(2026, 2, 5);
        List<Event> aliceNextWeekEvents = calendarService.getEventsForUser(alice, nextWeekDate);
        System.out.println("Alice's events on " + nextWeekDate + ":");
        for (Event event : aliceNextWeekEvents) {
            System.out.println("  - " + event.getTitle() + " at " + event.getTimeSlot() + 
                             " [Status: " + event.getStatus() + ", Type: " + event.getRecurrenceType() + "]");
        }

        System.out.println("\n==============================================");
        System.out.println("      CALENDAR APP DEMO COMPLETED");
        System.out.println("==============================================");
    }
}
