package Database.commands;

import Database.DatabaseController;
import Database.models.Schema;

/**
 * Command to create a new table
 */
public class CreateTableCommand implements Command {
    private final String tableName;
    private final Schema schema;
    
    public CreateTableCommand(String tableName, Schema schema) {
        this.tableName = tableName;
        this.schema = schema;
    }
    
    @Override
    public void execute(DatabaseController controller) {
        controller.getCurrentDatabase().createTable(tableName, schema);
        System.out.println("Table '" + tableName + "' created successfully");
    }
    
    @Override
    public String getDescription() {
        return "CREATE TABLE " + tableName;
    }
}
