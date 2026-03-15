package Database.models;

/**
 * Interface defining operations for different data types in the database.
 * Follows Strategy Pattern for extensibility.
 */
public interface DataType {
    /**
     * Validates if the given value is compatible with this data type
     */
    boolean validate(Object value);
    
    /**
     * Parses a string representation to the actual type
     */
    Object parse(String value) throws IllegalArgumentException;
    
    /**
     * Compares two values of this type
     * Returns negative if a < b, 0 if equal, positive if a > b
     */
    int compare(Object a, Object b);
    
    /**
     * Returns the type name
     */
    String getTypeName();
}
