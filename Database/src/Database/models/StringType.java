package Database.models;

/**
 * String data type implementation
 */
public class StringType implements DataType {
    private final int maxLength;
    
    public StringType() {
        this.maxLength = 255; // default
    }
    
    public StringType(int maxLength) {
        this.maxLength = maxLength;
    }
    
    @Override
    public boolean validate(Object value) {
        if (!(value instanceof String)) {
            return false;
        }
        String str = (String) value;
        return str.length() <= maxLength;
    }
    
    @Override
    public Object parse(String value) throws IllegalArgumentException {
        if (value.length() > maxLength) {
            throw new IllegalArgumentException("String exceeds max length of " + maxLength);
        }
        return value;
    }
    
    @Override
    public int compare(Object a, Object b) {
        if (!validate(a) || !validate(b)) {
            throw new IllegalArgumentException("Cannot compare non-string values");
        }
        return ((String) a).compareTo((String) b);
    }
    
    @Override
    public String getTypeName() {
        return "STRING(" + maxLength + ")";
    }
}
