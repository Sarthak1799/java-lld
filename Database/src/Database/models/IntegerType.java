package Database.models;

/**
 * Integer data type implementation
 */
public class IntegerType implements DataType {
    
    @Override
    public boolean validate(Object value) {
        return value instanceof Integer;
    }
    
    @Override
    public Object parse(String value) throws IllegalArgumentException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value: " + value);
        }
    }
    
    @Override
    public int compare(Object a, Object b) {
        if (!validate(a) || !validate(b)) {
            throw new IllegalArgumentException("Cannot compare non-integer values");
        }
        return Integer.compare((Integer) a, (Integer) b);
    }
    
    @Override
    public String getTypeName() {
        return "INTEGER";
    }
}
