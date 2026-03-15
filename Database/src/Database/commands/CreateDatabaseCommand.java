package Database.commands;

import Database.DatabaseController;

/**
 * Command to create a new database
 */
public class CreateDatabaseCommand implements Command {
    private final String databaseName;
    
    public CreateDatabaseCommand(String databaseName) {
        this.databaseName = databaseName;
    }
    
    @Override
    public void execute(DatabaseController controller) {
        controller.createDatabase(databaseName);
    }
    
    @Override
    public String getDescription() {
        return "CREATE DATABASE " + databaseName;
    }
}
