# Splitwise Class Diagram

## Core Domain Models

### 1. User
```
┌─────────────────────────────┐
│          User               │
├─────────────────────────────┤
│ - userId: String            │
│ - name: String              │
│ - email: String             │
│ - phone: String             │
│ - groupIds: List<String>    │
├─────────────────────────────┤
│ + User(...)                 │
│ + getUserId(): String       │
│ + getName(): String         │
│ + getEmail(): String        │
│ + getPhone(): String        │
│ + getGroupIds(): List       │
│ + addGroup(groupId): void   │
└─────────────────────────────┘
```

### 2. Expense
```
┌──────────────────────────────────┐
│          Expense                 │
├──────────────────────────────────┤
│ - expenseId: String              │
│ - description: String            │
│ - totalAmount: double            │
│ - paidBy: String                 │
│ - splits: List<Split>            │
│ - expenseType: ExpenseType       │
│ - groupId: String                │
│ - createdAt: Date                │
├──────────────────────────────────┤
│ + Expense(...)                   │
│ + validate(): boolean            │
│ + getExpenseId(): String         │
│ + getPaidBy(): String            │
│ + getSplits(): List<Split>       │
│ + getTotalAmount(): double       │
└──────────────────────────────────┘
```

### 3. Group
```
┌─────────────────────────────────┐
│          Group                  │
├─────────────────────────────────┤
│ - groupId: String               │
│ - groupName: String             │
│ - memberIds: List<String>       │
│ - expenseIds: List<String>      │
├─────────────────────────────────┤
│ + Group(groupId, groupName)     │
│ + addMember(userId): void       │
│ + addExpense(expenseId): void   │
│ + getMemberIds(): List          │
│ + getExpenseIds(): List         │
└─────────────────────────────────┘
```

### 4. Balance
```
┌─────────────────────────────┐
│         Balance             │
├─────────────────────────────┤
│ - userId1: String           │
│ - userId2: String           │
│ - amount: double            │
├─────────────────────────────┤
│ + Balance(...)              │
│ + getUserId1(): String      │
│ + getUserId2(): String      │
│ + getAmount(): double       │
│ + setAmount(amount): void   │
└─────────────────────────────┘
```

## Split Strategies (Strategy Pattern)

### Abstract Base Class
```
┌─────────────────────────────┐
│   Split (Abstract)          │
├─────────────────────────────┤
│ # userId: String            │
│ # amount: double            │
├─────────────────────────────┤
│ + Split(userId)             │
│ + getUserId(): String       │
│ + getAmount(): double       │
│ + setAmount(amount): void   │
└─────────────────────────────┘
              ▲
              │ extends
    ┌─────────┼─────────┐
    │         │         │
┌───────┐ ┌────────┐ ┌─────────────┐
│ Equal │ │ Exact  │ │   Percent   │
│ Split │ │ Split  │ │    Split    │
└───────┘ └────────┘ └─────────────┘
```

### 1. EqualSplit
- **Extends:** Split
- **Constructor:** EqualSplit(userId)
- **Behavior:** Amount is calculated by dividing total equally

### 2. ExactSplit
- **Extends:** Split
- **Constructor:** ExactSplit(userId, amount)
- **Behavior:** Amount is specified explicitly

### 3. PercentSplit
- **Extends:** Split
- **Fields:** percent: double
- **Constructor:** PercentSplit(userId, percent)
- **Behavior:** Amount is calculated as percentage of total

## Service Layer (Singleton Pattern)

### 0. SplitwiseService (Facade/Orchestrator - Singleton)
```
┌─────────────────────────────────────────────┐
│      SplitwiseService (Facade)              │
├─────────────────────────────────────────────┤
│ - userService: UserService                  │
│ - groupService: GroupService                │
│ - expenseService: ExpenseService            │
│ - balanceService: BalanceService            │
│ - static instance: SplitwiseService         │
├─────────────────────────────────────────────┤
│ + getInstance(): SplitwiseService           │
│                                             │
│ // User Operations                          │
│ + addUser(user): void                       │
│ + getUser(userId): User                     │
│ + getAllUsers(): Map                        │
│                                             │
│ // Group Operations                         │
│ + createGroup(group): void                  │
│ + getGroup(groupId): Group                  │
│ + addMemberToGroup(...): void               │
│                                             │
│ // Expense Operations                       │
│ + addExpense(...): void                     │
│ + showGroupExpenses(groupId): void          │
│ + showAllExpenses(): void                   │
│                                             │
│ // Balance Operations                       │
│ + settleBalance(...): void                  │
│ + getUserBalances(userId): List<Balance>    │
│ + showUserBalances(userId): void            │
│ + showAllBalances(): void                   │
│                                             │
│ // Composite Operations                     │
│ + addUserToGroup(user, groupId): void       │
│ + createGroupWithMembers(...): void         │
│ + showCompleteUserSummary(userId): void     │
└─────────────────────────────────────────────┘
```

### 1. UserService (Singleton)
```
┌────────────────────────────────────┐
│      UserService                   │
├────────────────────────────────────┤
│ - users: Map<String, User>         │
│ - static instance: UserService     │
├────────────────────────────────────┤
│ + getInstance(): UserService       │
│ + addUser(user): void              │
│ + getUser(userId): User            │
│ + getAllUsers(): Map               │
│ + userExists(userId): boolean      │
└────────────────────────────────────┘
```

### 2. GroupService (Singleton)
```
┌────────────────────────────────────┐
│      GroupService                  │
├────────────────────────────────────┤
│ - groups: Map<String, Group>       │
│ - static instance: GroupService    │
├────────────────────────────────────┤
│ + getInstance(): GroupService      │
│ + createGroup(group): void         │
│ + getGroup(groupId): Group         │
│ + addMemberToGroup(...): void      │
│ + getAllGroups(): Map              │
└────────────────────────────────────┘
```

### 3. ExpenseService (Singleton)
```
┌────────────────────────────────────────┐
│      ExpenseService                    │
├────────────────────────────────────────┤
│ - expenses: List<Expense>              │
│ - expenseCounter: int                  │
│ - static instance: ExpenseService      │
├────────────────────────────────────────┤
│ + getInstance(): ExpenseService        │
│ + addExpense(...): void                │
│ - processSplits(...): void             │
│ + showGroupExpenses(groupId): void     │
│ + getAllExpenses(): List<Expense>      │
│ + showAllExpenses(): void              │
└────────────────────────────────────────┘
```

### 4. BalanceService (Singleton)
```
┌──────────────────────────────────────────────┐
│      BalanceService                          │
├──────────────────────────────────────────────┤
│ - balances: Map<String, Map<String, Double>> │
│ - static instance: BalanceService            │
├──────────────────────────────────────────────┤
│ + getInstance(): BalanceService              │
│ + updateBalance(...): void                   │
│ + settleBalance(...): void                   │
│ + getUserBalances(userId): List<Balance>     │
│ + showUserBalances(userId): void             │
│ + showAllBalances(): void                    │
└──────────────────────────────────────────────┘
```

## Enums

### ExpenseType
```
┌─────────────────┐
│  ExpenseType    │
├─────────────────┤
│ INDIVIDUAL      │
│ GROUP           │
└─────────────────┘
```

## Relationships Between Classes

### 1. Inheritance Hierarchy
```
Split (Abstract Base Class)
  │
  ├── EqualSplit
  ├── ExactSplit
  └── PercentSplit
```

### 2. Composition Relationships
- **Expense** ◆──→ **Split** (1 to many)
  - An expense contains multiple splits
  - Splits cannot exist without an expense
  
### 3. Aggregation Relationships
- **Group** ◇──→ **User** (1 to many)
  - A group has multiple user members
  - Users can exist independently of groups
  
- **Group** ◇──→ **Expense** (1 to many)
  - A group has multiple expenses
  - Expenses can exist independently (individual expenses)

### 4. Usage/Dependency Relationships

#### Service Dependencies
```
Main ───uses───> SplitwiseService (Facade)
                      │
                      ├───delegates to───> UserService
                      ├───delegates to───> GroupService
                      ├───delegates to───> ExpenseService
                      └───delegates to───> BalanceService

ExpenseService ───uses───> BalanceService
                └─uses───> GroupService
                └─uses───> UserService

BalanceService ───uses───> UserService

GroupService ───uses───> UserService
```

#### Model Dependencies
```
Expense ───uses───> Split
        └─uses───> ExpenseType

Balance ───represents relationship between───> User
```

## Design Patterns Used

### 1. Facade Pattern
**Purpose:** Provide a unified, simplified interface to a complex subsystem of services

**Implementation:**
- **Facade:** SplitwiseService
- **Subsystems:** UserService, GroupService, ExpenseService, BalanceService

**Benefits:**
- Simplifies client code (Main only interacts with SplitwiseService)
- Hides complexity of individual services
- Enables composite operations across multiple services
- Loose coupling between client and service layer
- Easy to add new orchestrated operations

**Example Composite Operations:**
- `addUserToGroup()` - combines user creation and group membership
- `createGroupWithMembers()` - creates group and adds all members at once
- `showCompleteUserSummary()` - shows user info, groups, and balances

### 2. Strategy Pattern
**Purpose:** Allow different split calculation strategies at runtime

**Implementation:**
- **Context:** ExpenseService
- **Strategy Interface:** Split (abstract class)
- **Concrete Strategies:** EqualSplit, ExactSplit, PercentSplit

**Benefits:**
- Easy to add new split types (e.g., ShareSplit, RatioSplit)
- Split logic is encapsulated in respective classes
- No modification needed to existing code when adding new strategies

### 3. Singleton Pattern
**Purpose:** Ensure single instance of services managing shared state

**Implementation:**
- SplitwiseService (Facade)
- UserService
- GroupService  
- ExpenseService
- BalanceService

**Benefits:**
- Single source of truth for data
- Centralized data management
- Controlled access to shared resources

## Class Responsibilities

| Class | Responsibility |
|-------|---------------|
| **User** | Represents a user entity with personal information |
| **Expense** | Represents an expense with amount, splits, and validation |
| **Group** | Represents a group of users who share expenses |
| **Balance** | Represents balance between two users |
| **Split** | Abstract strategy for different split types |
| **UserService** | Manages user lifecycle and retrieval |
| **GroupService** | Manages group creation and membership |
| **ExpenseService** | Handles expense creation, validation, and reporting |
| **BalanceService** | Tracks and manages balances, handles settlements |

## Cardinality

- User 1 ←→ * Group (User can be in multiple groups)
- Group 1 ←→ * Expense (Group can have multiple expenses)
- Expense 1 ←→ * Split (Each expense has multiple splits)
- User 1 ←→ * Balance (User can have balances with multiple users)
