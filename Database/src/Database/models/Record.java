package Database.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single row/record in a table
 */
public class Record {
    private final List<Value> values;
    
    public Record(List<Value> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Record must have at least one value");
        }
        this.values = new ArrayList<>(values);
    }
    
    public List<Value> getValues() {
        return Collections.unmodifiableList(values);
    }
    
    public Value getValue(int index) {
        return values.get(index);
    }
    
    public int getValueCount() {
        return values.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(values.get(i).toString());
        }
        sb.append(")");
        return sb.toString();
    }
}
