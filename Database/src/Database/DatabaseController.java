package Database;

import Database.models.Database;
import java.util.HashMap;
import java.util.Map;

/**
 * Main controller for the database system (Singleton pattern)
 * Acts as a Facade to manage multiple databases
 */
public class DatabaseController {
    private static DatabaseController instance;
    private final Map<String, Database> databases;
    private Database currentDatabase;
    
    private DatabaseController() {
        this.databases = new HashMap<>();
        this.currentDatabase = null;
    }
    
    /**
     * Get singleton instance
     */
    public static DatabaseController getInstance() {
        if (instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }
    
    /**
     * Create a new database
     */
    public void createDatabase(String databaseName) {
        if (databases.containsKey(databaseName.toLowerCase())) {
            throw new IllegalArgumentException("Database " + databaseName + " already exists");
        }
        databases.put(databaseName.toLowerCase(), new Database(databaseName));
        System.out.println("Database '" + databaseName + "' created successfully");
    }
    
    /**
     * Use/select a database
     */
    public void useDatabase(String databaseName) {
        Database db = databases.get(databaseName.toLowerCase());
        if (db == null) {
            throw new IllegalArgumentException("Database " + databaseName + " not found");
        }
        currentDatabase = db;
        System.out.println("Using database: " + databaseName);
    }
    
    /**
     * Get current active database
     */
    public Database getCurrentDatabase() {
        if (currentDatabase == null) {
            throw new IllegalStateException("No database selected. Use 'USE <database>' first");
        }
        return currentDatabase;
    }
    
    /**
     * Check if a database exists
     */
    public boolean hasDatabase(String databaseName) {
        return databases.containsKey(databaseName.toLowerCase());
    }
    
    /**
     * List all databases
     */
    public void listDatabases() {
        if (databases.isEmpty()) {
            System.out.println("No databases found");
            return;
        }
        
        System.out.println("Databases:");
        for (String dbName : databases.keySet()) {
            String current = (currentDatabase != null && 
                            currentDatabase.getName().equalsIgnoreCase(dbName)) ? " (current)" : "";
            System.out.println("  - " + dbName + current);
        }
    }
}
