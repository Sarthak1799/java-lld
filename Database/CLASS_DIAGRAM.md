# Relational Database - Class Diagram

## Architecture Overview

```
┌─────────────────────┐
│  CommandParser      │
│  (Command Pattern)  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ DatabaseController  │──────┐
│  - databases: Map   │      │
└──────────┬──────────┘      │
           │                 │
           │ 1:N             │
           ▼                 │
┌─────────────────────┐      │
│     Database        │      │
│  - name: String     │      │
│  - tables: Map      │      │
└──────────┬──────────┘      │
           │                 │
           │ 1:N             │
           ▼                 │
┌─────────────────────┐      │
│      Table          │      │
│  - name: String     │◄─────┘
│  - schema: Schema   │
│  - records: List    │
└──────────┬──────────┘
           │
           │ has
           ▼
┌─────────────────────┐
│      Schema         │
│  - columns: List    │
└──────────┬──────────┘
           │
           │ 1:N
           ▼
┌─────────────────────┐
│      Column         │
│  - name: String     │
│  - dataType: Type   │
│  - constraints      │
└──────────┬──────────┘
           │
           │
           ▼
     ┌────────────┐
     │ DataType   │◄─────────┐
     │ <<interface>>         │
     └────────────┘          │
           △                 │
           │                 │
    ┌──────┴──────┐          │
    │             │          │
┌───────┐    ┌─────────┐    │
│Integer│    │ String  │    │
│ Type  │    │  Type   │    │
└───────┘    └─────────┘    │
                             │
┌─────────────────────┐      │
│      Record         │      │
│  - values: List     │      │
└──────────┬──────────┘      │
           │                 │
           │ 1:N             │
           ▼                 │
┌─────────────────────┐      │
│       Value         │      │
│  - data: Object     │      │
│  - type: DataType   │──────┘
└─────────────────────┘

┌─────────────────────┐
│   QueryEngine       │
│  (Strategy Pattern) │
└─────────────────────┘
```

## Class Relationships

### 1. Core Components

**DatabaseController** (Singleton/Facade)
- Manages multiple databases
- Entry point for all operations
- Routes commands to appropriate database

**Database**
- Contains multiple tables
- Namespace for tables

**Table**
- Has a Schema (defines structure)
- Contains Records (data)
- Enforces schema constraints

### 2. Schema Components

**Schema**
- Composition of Columns
- Defines table structure

**Column**
- Name + DataType + Constraints
- Reusable component

**DataType** (Interface - Strategy Pattern)
- `validate(Object value): boolean`
- `parse(String value): Object`
- `compare(Object a, Object b): int`
- Implementations: IntegerType, StringType, BooleanType, DateType

### 3. Data Components

**Record**
- List of Values
- Represents one row

**Value**
- Encapsulates data + type
- Type-safe wrapper

### 4. Command Processing

**CommandParser**
- Parses SQL-like commands
- Returns Command objects (Command Pattern)

**Command** (Interface)
- `execute(DatabaseController): Result`
- Implementations:
  - CreateDatabaseCommand
  - CreateTableCommand
  - InsertCommand
  - SelectCommand
  - etc.

### 5. Query Processing

**QueryEngine**
- Handles SELECT queries
- WHERE clause evaluation
- JOIN operations (basic)
- Uses Visitor/Strategy pattern for conditions

## Design Patterns Applied

1. **Singleton**: DatabaseController
2. **Factory**: DataType creation
3. **Strategy**: DataType implementations, Query conditions
4. **Command**: SQL command execution
5. **Composite**: Schema composed of Columns
6. **Builder**: For complex queries
7. **Facade**: DatabaseController as simplified interface

## SOLID Principles

**Single Responsibility**
- Each class has one clear purpose
- DataType only handles validation
- Table only manages records
- Schema only defines structure

**Open/Closed**
- DataType interface allows new types without modifying existing code
- Command pattern allows new commands easily

**Liskov Substitution**
- All DataType implementations interchangeable
- All Command implementations interchangeable

**Interface Segregation**
- Small, focused interfaces (DataType, Command)
- Clients depend only on what they need

**Dependency Inversion**
- High-level modules (Table) depend on abstractions (DataType)
- Not on concrete implementations
