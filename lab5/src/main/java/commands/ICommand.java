package commands;

import java.io.IOException;

/**
 * Interface for all commands
 */
public interface ICommand {
    String getDescription();

    String getName();

    void execute(String[] arg) throws IOException;
}
