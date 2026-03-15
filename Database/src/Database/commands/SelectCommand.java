package Database.commands;

import Database.DatabaseController;
import Database.models.Record;
import Database.models.Table;
import java.util.List;

/**
 * Command to select/query records from a table
 */
public class SelectCommand implements Command {
    private final String tableName;
    private final String whereColumn;
    private final Object whereValue;
    
    // SELECT * FROM table
    public SelectCommand(String tableName) {
        this(tableName, null, null);
    }
    
    // SELECT * FROM table WHERE column = value
    public SelectCommand(String tableName, String whereColumn, Object whereValue) {
        this.tableName = tableName;
        this.whereColumn = whereColumn;
        this.whereValue = whereValue;
    }
    
    @Override
    public void execute(DatabaseController controller) {
        Table table = controller.getCurrentDatabase().getTable(tableName);
        
        List<Record> results;
        if (whereColumn == null) {
            results = table.selectAll();
        } else {
            results = table.select(whereColumn, whereValue);
        }
        
        // Print results
        table.printRecords();
    }
    
    @Override
    public String getDescription() {
        String desc = "SELECT * FROM " + tableName;
        if (whereColumn != null) {
            desc += " WHERE " + whereColumn + " = " + whereValue;
        }
        return desc;
    }
}
