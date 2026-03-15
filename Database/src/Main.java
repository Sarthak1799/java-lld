import Database.CommandParser;
import Database.DatabaseController;
import Database.commands.Command;
import java.util.Scanner;

/**
 * Main class to demonstrate the relational database system
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Simplified Relational Database System ===\n");
        
        // Get the database controller instance
        DatabaseController controller = DatabaseController.getInstance();
        
        // Demo mode or interactive mode
        if (args.length > 0 && args[0].equals("--interactive")) {
            interactiveMode(controller);
        } else {
            demoMode(controller);
        }
    }
    
    /**
     * Run predefined demo commands
     */
    private static void demoMode(DatabaseController controller) {
        System.out.println("Running in DEMO mode...\n");
        
        String[] commands = {
            "CREATE DATABASE school",
            "USE school",
            "CREATE TABLE students (id INTEGER PRIMARY KEY, name STRING(50) NOT NULL, age INTEGER, gpa INTEGER)",
            "INSERT INTO students VALUES (1, 'Alice Johnson', 20, 85)",
            "INSERT INTO students VALUES (2, 'Bob Smith', 22, 92)",
            "INSERT INTO students VALUES (3, 'Charlie Brown', 21, 78)",
            "SELECT * FROM students",
            "",
            "CREATE TABLE courses (course_id INTEGER PRIMARY KEY, course_name STRING(100), credits INTEGER)",
            "INSERT INTO courses VALUES (101, 'Data Structures', 4)",
            "INSERT INTO courses VALUES (102, 'Database Systems', 3)",
            "INSERT INTO courses VALUES (103, 'Operating Systems', 4)",
            "SELECT * FROM courses",
            "",
            "SELECT * FROM students WHERE age = 21"
        };
        
        for (String commandStr : commands) {
            if (commandStr.isEmpty()) {
                System.out.println();
                continue;
            }
            
            executeCommand(controller, commandStr);
        }
        
        System.out.println("\n=== Demo completed successfully! ===");
        System.out.println("\nTo run in interactive mode, use: java Main --interactive");
    }
    
    /**
     * Run in interactive mode with user input
     */
    private static void interactiveMode(DatabaseController controller) {
        System.out.println("Running in INTERACTIVE mode...");
        System.out.println("Enter SQL commands (type 'EXIT' to quit, 'HELP' for commands)\n");
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("db> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            if (input.equalsIgnoreCase("EXIT") || input.equalsIgnoreCase("QUIT")) {
                System.out.println("Goodbye!");
                break;
            }
            
            if (input.equalsIgnoreCase("HELP")) {
                printHelp();
                continue;
            }
            
            if (input.equalsIgnoreCase("SHOW DATABASES")) {
                controller.listDatabases();
                continue;
            }
            
            if (input.equalsIgnoreCase("SHOW TABLES")) {
                try {
                    controller.getCurrentDatabase().listTables();
                } catch (IllegalStateException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                continue;
            }
            
            executeCommand(controller, input);
        }
        
        scanner.close();
    }
    
    /**
     * Execute a single command
     */
    private static void executeCommand(DatabaseController controller, String commandStr) {
        System.out.println("Executing: " + commandStr);
        
        try {
            Command command = CommandParser.parse(commandStr);
            if (command != null) {
                command.execute(controller);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Print help information
     */
    private static void printHelp() {
        System.out.println("\n=== Supported Commands ===");
        System.out.println("CREATE DATABASE <name>");
        System.out.println("USE <database>");
        System.out.println("CREATE TABLE <name> (<column> <type> [constraints], ...)");
        System.out.println("  Types: INTEGER, STRING(n), BOOLEAN");
        System.out.println("  Constraints: PRIMARY KEY, NOT NULL");
        System.out.println("INSERT INTO <table> VALUES (<value1>, <value2>, ...)");
        System.out.println("SELECT * FROM <table>");
        System.out.println("SELECT * FROM <table> WHERE <column> = <value>");
        System.out.println();
        System.out.println("=== Special Commands ===");
        System.out.println("SHOW DATABASES");
        System.out.println("SHOW TABLES");
        System.out.println("HELP - Show this help");
        System.out.println("EXIT - Quit the program");
        System.out.println();
    }
}
