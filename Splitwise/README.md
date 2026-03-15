# Splitwise Low-Level Design

A Java implementation of a Splitwise-like expense sharing application demonstrating object-oriented design principles and design patterns.

## Features

1. **User Management**: Create and manage users
2. **Group Management**: Create groups and add members
3. **Expense Management**: 
   - Add individual expenses
   - Add group expenses
   - Support multiple split strategies (Equal, Exact, Percent)
4. **Balance Tracking**: Track who owes whom and how much
5. **Settlement**: Settle balances between users
6. **Expense Reports**: View group expenses and individual balances

## Architecture & Design Patterns

### Design Patterns Used

1. **Facade Pattern**:
   - `SplitwiseService` acts as an orchestrator/facade layer
   - Provides unified interface to all service subsystems
   - Simplifies client code by hiding service complexities
   - Enables composite operations across multiple services

2. **Strategy Pattern**: 
   - `Split` abstract class with concrete implementations (`EqualSplit`, `ExactSplit`, `PercentSplit`)
   - Allows flexible split strategies without modifying core logic
   - Easy to add new split types (e.g., ShareSplit, RatioSplit)

3. **Singleton Pattern**:
   - All service classes use Singleton pattern (including SplitwiseService)
   - Ensures single source of truth for data management
   - Thread-safe access to shared resources

### SOLID Principles

1. **Single Responsibility Principle (SRP)**:
   - Each class has one clear responsibility
   - `UserService` manages users
   - `GroupService` manages groups
   - `ExpenseService` manages expenses
   - `BalanceService` manages balances

2. **Open/Closed Principle (OCP)**:
   - Split strategies are open for extension but closed for modification
   - New split types can be added without changing existing code

3. **Liskov Substitution Principle (LSP)**:
   - All Split subclasses can substitute the base Split class
   - Polymorphic behavior in expense processing

4. **Interface Segregation Principle (ISP)**:
   - Services expose only relevant methods
   - Clean and focused interfaces

5. **Dependency Inversion Principle (DIP)**:
   - High-level modules (ExpenseService) depend on abstractions (Split)
   - Not directly dependent on concrete implementations

## Project Structure

```
Splitwise/
├── src/
│   ├── Main.java
│   ├── models/
│   │   ├── User.java
│   │   ├── Expense.java
│   │   ├── Group.java
│   │   └── Balance.java
│   ├── splitstrategies/
│   │   ├── Split.java (abstract)
│   │   ├── EqualSplit.java
│   │   ├── ExactSplit.java
│   │   └── PercentSplit.java
│   ├── services/
│   │   ├── SplitwiseService.java (Facade/Orchestrator)
│   │   ├── UserService.java
│   │   ├── GroupService.java
│   │   ├── ExpenseService.java
│   │   └── BalanceService.java
│   └── enums/
│       └── ExpenseType.java
```

## Class Descriptions

### Models
- **User**: Represents a user with basic information
- **Expense**: Represents an expense with amount, payer, and splits
- **Group**: Represents a group of users who share expenses
- **Balance**: Represents the balance between two users

### Split Strategies
- **Split**: Abstract base class for all split strategies
- **EqualSplit**: Splits expense equally among all participants
- **ExactSplit**: Each participant pays an exact specified amount
- **PercentSplit**: Each participant pays a percentage of total amount

### Services
- **SplitwiseService**: Facade/Orchestrator that provides unified interface to all services
- **UserService**: Manages user creation and retrieval
- **GroupService**: Manages group creation and membership
- **ExpenseService**: Handles expense creation and validation
- **BalanceService**: Tracks and manages balances between users

## How to Run

```bash
cd Splitwise/src
javac Main.java
java Main
```

## Example Usage

```java
// Get the orchestrator service
SplitwiseService splitwise = SplitwiseService.getInstance();

// Create users
User alice = new User("U1", "Alice", "alice@example.com", "1234567890");
User bob = new User("U2", "Bob", "bob@example.com", "9876543210");
splitwise.addUser(alice);
splitwise.addUser(bob);

// Create group
Group roommates = new Group("G1", "Roommates");
roommates.addMember("U1");
roommates.addMember("U2");
splitwise.createGroup(roommates);

// Add equal split expense
List<Split> splits = Arrays.asList(
    new EqualSplit("U1"),
    new EqualSplit("U2")
);
splitwise.addExpense("Rent", 2000.0, "U1", splits, ExpenseType.GROUP, "G1");

// Settle balance
splitwise.settleBalance("U2", "U1", 1000.0);

// Show complete user summary
splitwise.showCompleteUserSummary("U1");
```

## Future Enhancements

The design is flexible and can easily accommodate:

1. **New Split Strategies**:
   - ShareSplit (based on number of shares)
   - RatioSplit (based on ratios like 2:3:1)
   - CustomSplit (custom logic)

2. **Additional Features**:
   - Expense categories (Food, Travel, Utilities, etc.)
   - Expense comments and attachments
   - Recurring expenses
   - Currency support
   - Simplify debts algorithm (minimize transactions)
   - Notification system
   - Audit trail and expense history

3. **Persistence**:
   - Add database layer
   - Repository pattern for data access

4. **Multi-tenancy**:
   - Support for multiple independent groups of users

## Design Decisions

1. **Balance Management**: Uses a nested map structure for O(1) balance lookups
2. **Validation**: Expenses are validated to ensure splits sum to total amount
3. **Immutability**: Expense objects are immutable after creation
4. **Separation of Concerns**: Clear separation between models, strategies, and services
5. **Extensibility**: Easy to add new features without modifying existing code

## Interview Discussion Points

- **Scalability**: How would you handle millions of users?
- **Concurrency**: How to handle concurrent expense additions?
- **Debt Simplification**: How to minimize number of transactions?
- **Database Design**: What would the schema look like?
- **API Design**: How would you expose this as a REST API?
- **Testing**: What unit tests would you write?
