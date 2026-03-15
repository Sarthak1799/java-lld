package Database.commands;

import Database.DatabaseController;
import java.util.List;

/**
 * Command to insert a record into a table
 */
public class InsertCommand implements Command {
    private final String tableName;
    private final List<Object> values;
    
    public InsertCommand(String tableName, List<Object> values) {
        this.tableName = tableName;
        this.values = values;
    }
    
    @Override
    public void execute(DatabaseController controller) {
        controller.getCurrentDatabase()
                  .getTable(tableName)
                  .insert(values);
        System.out.println("Record inserted into table '" + tableName + "'");
    }
    
    @Override
    public String getDescription() {
        return "INSERT INTO " + tableName + " VALUES " + values;
    }
}
