# Calendar App - Low Level Design

A comprehensive Java-based calendar application implementing various design patterns and best practices.

## Features

- ✅ Create events with location, start time, and end time
- ✅ Recurring events (Daily, Weekly, Monthly)
- ✅ Edit and delete events
- ✅ Multi-user event creation with availability checking
- ✅ Notifications using Observer pattern
- ✅ Time advancement with automatic event processing
- ✅ Common availability slot finder

## Architecture

### Entities

- **User** (1:1 Calendar) - Represents a system user
- **Calendar** (1:m Event) - Manages user's events
- **Event** (Abstract) - Base class for all event types
  - OneTimeEvent
  - DailyEvent
  - WeeklyEvent
  - MonthlyEvent
- **TimeSlot** - Represents a time range

### Services

- **UserService** - Manages users and implements EventObserver
- **CalendarService** - Manages calendars and events (Subject in Observer pattern)

### Design Patterns Used

1. **Singleton Pattern** - UserService and CalendarService
2. **Observer Pattern** - Event notification system
3. **Abstract Factory Pattern** - Event creation
4. **Strategy Pattern** - Different recurrence strategies via getNextEvent()

## Project Structure

```
CalenderApp/
├── src/
│   ├── enums/
│   │   ├── RecurrenceType.java
│   │   ├── EventStatus.java
│   │   └── NotificationType.java
│   ├── models/
│   │   ├── User.java
│   │   ├── Calendar.java
│   │   ├── TimeSlot.java
│   │   ├── Event.java (abstract)
│   │   ├── OneTimeEvent.java
│   │   ├── DailyEvent.java
│   │   ├── WeeklyEvent.java
│   │   └── MonthlyEvent.java
│   ├── observers/
│   │   └── EventObserver.java
│   ├── services/
│   │   ├── UserService.java
│   │   └── CalendarService.java
│   └── Main.java
└── README.md
```

## Key Features Explained

### 1. Event Creation with Availability Check

The system checks all participants' calendars before creating an event to ensure no conflicts.

### 2. Observer Pattern for Notifications

When events are created, updated, deleted, or completed, all participants receive notifications.

### 3. Recurring Events

Abstract `Event` class has `getNextEvent()` method that concrete implementations override to generate next occurrences.

### 4. Time Advancement (Step Function)

The `step(date)` function:
- Advances system time to the specified date
- Marks all completed events
- Generates next occurrences for recurring events
- Notifies all participants

### 5. Common Availability Finder

Finds time slots where all specified users are available on a given date.

## How to Run

```bash
# Navigate to src directory
cd src

# Compile all Java files
javac -d ../bin enums/*.java models/*.java observers/*.java services/*.java Main.java

# Run the application
cd ../bin
java Main
```

Or simply:

```bash
cd src
javac Main.java enums/*.java models/*.java observers/*.java services/*.java
java Main
```

## Usage Example

```java
// Initialize services
UserService userService = UserService.getInstance();
CalendarService calendarService = CalendarService.getInstance();
calendarService.registerObserver(userService);

// Create users
User alice = userService.createUser("Alice", "alice@example.com");
User bob = userService.createUser("Bob", "bob@example.com");

// Create event
TimeSlot slot = new TimeSlot(
    LocalDateTime.of(2026, 1, 28, 10, 0),
    LocalDateTime.of(2026, 1, 28, 11, 0)
);

Event meeting = calendarService.createEvent(
    "Team Meeting",
    "Daily standup",
    "Room A",
    slot,
    Arrays.asList(alice, bob),
    "WEEKLY"
);

// Advance time
calendarService.step(LocalDateTime.of(2026, 2, 5, 18, 0));
```

## Requirements Fulfilled

- ✅ User can create events with location, start/end time
- ✅ Recurring events (daily, weekly, monthly)
- ✅ Edit and delete events
- ✅ Multi-user events with availability checking
- ✅ Notifications on event updates
- ✅ Observer pattern implementation
- ✅ Abstract Event class with getNextEvent()
- ✅ step() function for time advancement

## Future Enhancements

- Add yearly recurring events
- Custom recurrence patterns (e.g., every 2 weeks)
- Event reminders at specific intervals
- Time zone support
- Event attachments and notes
- Calendar sharing and permissions
- Integration with external calendar services
