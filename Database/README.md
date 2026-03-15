# Relational Database System - Implementation Complete!

## Project Structure
```
src/
├── Main.java                           # Entry point with demo & interactive modes
├── Database/
│   ├── DatabaseController.java        # Singleton controller (Facade pattern)
│   ├── CommandParser.java             # SQL-like command parser
│   ├── commands/
│   │   ├── Command.java               # Command interface
│   │   ├── CreateDatabaseCommand.java
│   │   ├── UseDatabaseCommand.java
│   │   ├── CreateTableCommand.java
│   │   ├── InsertCommand.java
│   │   └── SelectCommand.java
│   └── models/
│       ├── DataType.java              # Strategy pattern interface
│       ├── IntegerType.java
│       ├── StringType.java
│       ├── BooleanType.java
│       ├── Value.java                 # Type-safe value wrapper
│       ├── Column.java
│       ├── Schema.java
│       ├── Record.java
│       ├── Table.java
│       └── Database.java
```

## How to Run

### Compile:
```bash
cd /home/dell/Desktop/projects/java-lld/Database
find src -name "*.java" | xargs javac -d bin -cp src
```

### Run Demo Mode:
```bash
java -cp bin Main
```

### Run Interactive Mode:
```bash
java -cp bin Main --interactive
```

## Supported SQL Commands

### Database Operations
- `CREATE DATABASE <name>` - Create a new database
- `USE <database>` - Switch to a database
- `SHOW DATABASES` - List all databases

### Table Operations
- `CREATE TABLE <name> (<columns>)` - Create a table with schema
  - Example: `CREATE TABLE users (id INTEGER PRIMARY KEY, name STRING(50) NOT NULL, age INTEGER)`
- `SHOW TABLES` - List tables in current database

### Data Operations
- `INSERT INTO <table> VALUES (<values>)` - Insert a record
  - Example: `INSERT INTO users VALUES (1, 'John', 25)`
- `SELECT * FROM <table>` - Query all records
- `SELECT * FROM <table> WHERE <column> = <value>` - Query with filter
  - Example: `SELECT * FROM users WHERE age = 25`

### Data Types Supported
- `INTEGER` or `INT` - Integer numbers
- `STRING(n)` or `VARCHAR(n)` - Variable length strings
- `BOOLEAN` or `BOOL` - True/false values

### Constraints Supported
- `PRIMARY KEY` - Primary key constraint
- `NOT NULL` - Non-nullable constraint

## Design Patterns Applied

1. **Singleton Pattern** - DatabaseController
2. **Command Pattern** - All SQL commands
3. **Strategy Pattern** - DataType implementations
4. **Facade Pattern** - DatabaseController as simplified interface
5. **Composition** - Schema composed of Columns, Record of Values

## SOLID Principles

✅ **Single Responsibility** - Each class has one clear purpose
✅ **Open/Closed** - Extensible via interfaces (DataType, Command)
✅ **Liskov Substitution** - All implementations are interchangeable
✅ **Interface Segregation** - Small, focused interfaces
✅ **Dependency Inversion** - Depends on abstractions, not concretions

## Features Implemented

✅ Create database  
✅ Create table with schema  
✅ Insert rows  
✅ Query rows (SELECT *)  
✅ Query with WHERE clause  
✅ Basic relational behavior  
✅ Command parsing  
✅ In-memory data structures  
✅ Type validation  
✅ Schema constraints  
✅ OOP + SOLID principles

## Demo Output

The system successfully:
- Creates databases
- Defines tables with typed schemas
- Inserts records with validation
- Queries data with and without filters
- Enforces constraints (NOT NULL, PRIMARY KEY)
- Validates data types at runtime

All requirements met! ✨
