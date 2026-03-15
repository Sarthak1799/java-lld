package Database.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a database containing multiple tables
 */
public class Database {
    private final String name;
    private final Map<String, Table> tables;
    
    public Database(String name) {
        this.name = name;
        this.tables = new HashMap<>();
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * Create a new table in this database
     */
    public void createTable(String tableName, Schema schema) {
        if (tables.containsKey(tableName.toLowerCase())) {
            throw new IllegalArgumentException("Table " + tableName + " already exists");
        }
        tables.put(tableName.toLowerCase(), new Table(tableName, schema));
    }
    
    /**
     * Get a table by name
     */
    public Table getTable(String tableName) {
        Table table = tables.get(tableName.toLowerCase());
        if (table == null) {
            throw new IllegalArgumentException("Table " + tableName + " not found");
        }
        return table;
    }
    
    /**
     * Check if table exists
     */
    public boolean hasTable(String tableName) {
        return tables.containsKey(tableName.toLowerCase());
    }
    
    /**
     * List all table names
     */
    public void listTables() {
        if (tables.isEmpty()) {
            System.out.println("No tables in database " + name);
            return;
        }
        
        System.out.println("Tables in database " + name + ":");
        for (String tableName : tables.keySet()) {
            System.out.println("  - " + tableName);
        }
    }
}
