package Database;

import Database.commands.*;
import Database.models.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses SQL-like commands and creates Command objects
 */
public class CommandParser {
    
    /**
     * Parse a command string and return a Command object
     */
    public static Command parse(String commandString) {
        commandString = commandString.trim();
        
        if (commandString.isEmpty()) {
            return null;
        }
        
        // Convert to uppercase for pattern matching (but preserve original for values)
        String upperCommand = commandString.toUpperCase();
        
        try {
            // CREATE DATABASE <name>
            if (upperCommand.startsWith("CREATE DATABASE")) {
                return parseCreateDatabase(commandString);
            }
            
            // USE <database>
            if (upperCommand.startsWith("USE ")) {
                return parseUseDatabase(commandString);
            }
            
            // CREATE TABLE <name> (columns...)
            if (upperCommand.startsWith("CREATE TABLE")) {
                return parseCreateTable(commandString);
            }
            
            // INSERT INTO <table> VALUES (...)
            if (upperCommand.startsWith("INSERT INTO")) {
                return parseInsert(commandString);
            }
            
            // SELECT * FROM <table>
            if (upperCommand.startsWith("SELECT")) {
                return parseSelect(commandString);
            }
            
            throw new IllegalArgumentException("Unknown command: " + commandString);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing command: " + e.getMessage());
        }
    }
    
    private static Command parseCreateDatabase(String command) {
        Pattern pattern = Pattern.compile("CREATE DATABASE\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);
        
        if (matcher.find()) {
            String dbName = matcher.group(1);
            return new CreateDatabaseCommand(dbName);
        }
        
        throw new IllegalArgumentException("Invalid CREATE DATABASE syntax");
    }
    
    private static Command parseUseDatabase(String command) {
        Pattern pattern = Pattern.compile("USE\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);
        
        if (matcher.find()) {
            String dbName = matcher.group(1);
            return new UseDatabaseCommand(dbName);
        }
        
        throw new IllegalArgumentException("Invalid USE syntax");
    }
    
    private static Command parseCreateTable(String command) {
        // Pattern: CREATE TABLE <name> (<column definitions>)
        Pattern pattern = Pattern.compile(
            "CREATE TABLE\\s+(\\w+)\\s*\\((.+)\\)", 
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(command);
        
        if (matcher.find()) {
            String tableName = matcher.group(1);
            String columnDefs = matcher.group(2);
            
            Schema schema = parseColumnDefinitions(columnDefs);
            return new CreateTableCommand(tableName, schema);
        }
        
        throw new IllegalArgumentException("Invalid CREATE TABLE syntax");
    }
    
    private static Schema parseColumnDefinitions(String columnDefs) {
        List<Column> columns = new ArrayList<>();
        String[] parts = columnDefs.split(",");
        
        for (String part : parts) {
            part = part.trim();
            String[] tokens = part.split("\\s+");
            
            if (tokens.length < 2) {
                throw new IllegalArgumentException("Invalid column definition: " + part);
            }
            
            String columnName = tokens[0];
            String typeName = tokens[1].toUpperCase();
            
            DataType dataType = parseDataType(typeName);
            
            // Check for constraints
            boolean nullable = true;
            boolean primaryKey = false;
            
            String remaining = part.toUpperCase();
            if (remaining.contains("NOT NULL")) {
                nullable = false;
            }
            if (remaining.contains("PRIMARY KEY")) {
                primaryKey = true;
                nullable = false;
            }
            
            columns.add(new Column(columnName, dataType, nullable, primaryKey));
        }
        
        return new Schema(columns);
    }
    
    private static DataType parseDataType(String typeName) {
        if (typeName.equals("INTEGER") || typeName.equals("INT")) {
            return new IntegerType();
        } else if (typeName.startsWith("STRING") || typeName.startsWith("VARCHAR")) {
            // Parse STRING(100) or VARCHAR(100)
            Pattern pattern = Pattern.compile("\\w+\\((\\d+)\\)");
            Matcher matcher = pattern.matcher(typeName);
            
            if (matcher.find()) {
                int length = Integer.parseInt(matcher.group(1));
                return new StringType(length);
            }
            return new StringType(); // default length
        } else if (typeName.equals("BOOLEAN") || typeName.equals("BOOL")) {
            return new BooleanType();
        }
        
        throw new IllegalArgumentException("Unknown data type: " + typeName);
    }
    
    private static Command parseInsert(String command) {
        // Pattern: INSERT INTO <table> VALUES (...)
        Pattern pattern = Pattern.compile(
            "INSERT INTO\\s+(\\w+)\\s+VALUES\\s*\\((.+)\\)", 
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(command);
        
        if (matcher.find()) {
            String tableName = matcher.group(1);
            String valuesStr = matcher.group(2);
            
            List<Object> values = parseValues(valuesStr);
            return new InsertCommand(tableName, values);
        }
        
        throw new IllegalArgumentException("Invalid INSERT syntax");
    }
    
    private static List<Object> parseValues(String valuesStr) {
        List<Object> values = new ArrayList<>();
        String[] parts = valuesStr.split(",");
        
        for (String part : parts) {
            part = part.trim();
            
            // String literal (quoted)
            if ((part.startsWith("'") && part.endsWith("'")) || 
                (part.startsWith("\"") && part.endsWith("\""))) {
                values.add(part.substring(1, part.length() - 1));
            }
            // Integer
            else if (part.matches("-?\\d+")) {
                values.add(Integer.parseInt(part));
            }
            // Boolean
            else if (part.equalsIgnoreCase("true") || part.equalsIgnoreCase("false")) {
                values.add(Boolean.parseBoolean(part));
            }
            // NULL
            else if (part.equalsIgnoreCase("null")) {
                values.add(null);
            }
            else {
                throw new IllegalArgumentException("Cannot parse value: " + part);
            }
        }
        
        return values;
    }
    
    private static Command parseSelect(String command) {
        // Pattern: SELECT * FROM <table> [WHERE <column> = <value>]
        Pattern pattern = Pattern.compile(
            "SELECT\\s+\\*\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+(\\w+)\\s*=\\s*(.+))?", 
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(command);
        
        if (matcher.find()) {
            String tableName = matcher.group(1);
            String whereColumn = matcher.group(2);
            String whereValueStr = matcher.group(3);
            
            if (whereColumn == null) {
                return new SelectCommand(tableName);
            } else {
                Object whereValue = parseSingleValue(whereValueStr.trim());
                return new SelectCommand(tableName, whereColumn, whereValue);
            }
        }
        
        throw new IllegalArgumentException("Invalid SELECT syntax");
    }
    
    private static Object parseSingleValue(String valueStr) {
        // String literal
        if ((valueStr.startsWith("'") && valueStr.endsWith("'")) || 
            (valueStr.startsWith("\"") && valueStr.endsWith("\""))) {
            return valueStr.substring(1, valueStr.length() - 1);
        }
        // Integer
        if (valueStr.matches("-?\\d+")) {
            return Integer.parseInt(valueStr);
        }
        // Boolean
        if (valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(valueStr);
        }
        
        return valueStr; // return as string if unsure
    }
}
