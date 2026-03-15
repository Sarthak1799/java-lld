package Database.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a table with schema and records
 */
public class Table {
    private final String name;
    private final Schema schema;
    private final List<Record> records;
    
    public Table(String name, Schema schema) {
        this.name = name;
        this.schema = schema;
        this.records = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public Schema getSchema() {
        return schema;
    }
    
    public List<Record> getRecords() {
        return Collections.unmodifiableList(records);
    }
    
    /**
     * Insert a new record with raw values
     */
    public void insert(List<Object> rawValues) {
        // Validate against schema
        schema.validateValues(rawValues);
        
        // Create Value objects
        List<Value> values = new ArrayList<>();
        for (int i = 0; i < rawValues.size(); i++) {
            Column column = schema.getColumn(i);
            values.add(new Value(rawValues.get(i), column.getDataType()));
        }
        
        Record record = new Record(values);
        records.add(record);
    }
    
    /**
     * Query all records
     */
    public List<Record> selectAll() {
        return new ArrayList<>(records);
    }
    
    /**
     * Query records with a simple WHERE condition
     */
    public List<Record> select(String columnName, Object value) {
        int columnIndex = schema.getColumnIndex(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column " + columnName + " not found");
        }
        
        return records.stream()
            .filter(record -> {
                Object recordValue = record.getValue(columnIndex).getData();
                return recordValue != null && recordValue.equals(value);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Get count of records
     */
    public int getRecordCount() {
        return records.size();
    }
    
    /**
     * Print table structure
     */
    public void printSchema() {
        System.out.println("Table: " + name);
        System.out.println(schema.toString());
    }
    
    /**
     * Print all records
     */
    public void printRecords() {
        System.out.println("\nTable: " + name);
        
        // Print column headers
        List<Column> columns = schema.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) System.out.print(" | ");
            System.out.print(columns.get(i).getName());
        }
        System.out.println();
        System.out.println("-".repeat(50));
        
        // Print records
        for (Record record : records) {
            List<Value> values = record.getValues();
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) System.out.print(" | ");
                System.out.print(values.get(i).toString());
            }
            System.out.println();
        }
        System.out.println("Total records: " + records.size());
    }
}
