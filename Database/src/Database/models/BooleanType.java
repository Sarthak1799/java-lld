package Database.models;

/**
 * Boolean data type implementation
 */
public class BooleanType implements DataType {
    
    @Override
    public boolean validate(Object value) {
        return value instanceof Boolean;
    }
    
    @Override
    public Object parse(String value) throws IllegalArgumentException {
        String trimmed = value.trim().toLowerCase();
        if (trimmed.equals("true") || trimmed.equals("1")) {
            return true;
        } else if (trimmed.equals("false") || trimmed.equals("0")) {
            return false;
        }
        throw new IllegalArgumentException("Invalid boolean value: " + value);
    }
    
    @Override
    public int compare(Object a, Object b) {
        if (!validate(a) || !validate(b)) {
            throw new IllegalArgumentException("Cannot compare non-boolean values");
        }
        return Boolean.compare((Boolean) a, (Boolean) b);
    }
    
    @Override
    public String getTypeName() {
        return "BOOLEAN";
    }
}
