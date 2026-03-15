package Database.models;

/**
 * Represents a column in a table schema
 */
public class Column {
    private final String name;
    private final DataType dataType;
    private final boolean nullable;
    private final boolean primaryKey;
    
    public Column(String name, DataType dataType) {
        this(name, dataType, true, false);
    }
    
    public Column(String name, DataType dataType, boolean nullable, boolean primaryKey) {
        this.name = name;
        this.dataType = dataType;
        this.nullable = nullable;
        this.primaryKey = primaryKey;
    }
    
    public String getName() {
        return name;
    }
    
    public DataType getDataType() {
        return dataType;
    }
    
    public boolean isNullable() {
        return nullable;
    }
    
    public boolean isPrimaryKey() {
        return primaryKey;
    }
    
    public void validate(Object value) {
        if (value == null) {
            if (!nullable) {
                throw new IllegalArgumentException("Column " + name + " cannot be null");
            }
            return;
        }
        
        if (!dataType.validate(value)) {
            throw new IllegalArgumentException(
                "Value " + value + " is not valid for column " + name + 
                " of type " + dataType.getTypeName()
            );
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(dataType.getTypeName());
        if (primaryKey) sb.append(" PRIMARY KEY");
        if (!nullable) sb.append(" NOT NULL");
        return sb.toString();
    }
}
