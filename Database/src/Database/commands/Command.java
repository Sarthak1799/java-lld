package Database.commands;

import Database.DatabaseController;

/**
 * Command interface following Command Pattern
 */
public interface Command {
    /**
     * Execute the command
     */
    void execute(DatabaseController controller);
    
    /**
     * Get command description
     */
    String getDescription();
}
