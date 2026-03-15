package Database.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the schema (structure) of a table
 */
public class Schema {
    private final List<Column> columns;
    
    public Schema(List<Column> columns) {
        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("Schema must have at least one column");
        }
        this.columns = new ArrayList<>(columns);
    }
    
    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }
    
    public int getColumnCount() {
        return columns.size();
    }
    
    public Column getColumn(int index) {
        return columns.get(index);
    }
    
    public Column getColumn(String name) {
        return columns.stream()
            .filter(col -> col.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public int getColumnIndex(String name) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }
    
    public void validateValues(List<Object> values) {
        if (values.size() != columns.size()) {
            throw new IllegalArgumentException(
                "Expected " + columns.size() + " values, got " + values.size()
            );
        }
        
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).validate(values.get(i));
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Schema: [");
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(columns.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
