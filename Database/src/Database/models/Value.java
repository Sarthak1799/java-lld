package Database.models;

/**
 * Represents a typed value in the database
 */
public class Value {
    private final Object data;
    private final DataType type;
    
    public Value(Object data, DataType type) {
        if (!type.validate(data)) {
            throw new IllegalArgumentException(
                "Value " + data + " is not valid for type " + type.getTypeName()
            );
        }
        this.data = data;
        this.type = type;
    }
    
    public Object getData() {
        return data;
    }
    
    public DataType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return data != null ? data.toString() : "NULL";
    }
}
