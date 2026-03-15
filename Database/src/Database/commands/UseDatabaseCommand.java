package Database.commands;

import Database.DatabaseController;

/**
 * Command to select/use a database
 */
public class UseDatabaseCommand implements Command {
    private final String databaseName;
    
    public UseDatabaseCommand(String databaseName) {
        this.databaseName = databaseName;
    }
    
    @Override
    public void execute(DatabaseController controller) {
        controller.useDatabase(databaseName);
    }
    
    @Override
    public String getDescription() {
        return "USE " + databaseName;
    }
}
