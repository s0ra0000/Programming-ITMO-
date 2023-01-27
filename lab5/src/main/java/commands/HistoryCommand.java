package commands;

import exceptions.IncorrectCommandInputException;
import utilities.CollectionManager;
import utilities.CommandManager;

import java.io.IOException;

/**
 * Concrete command, History of executed commands
 */
public class HistoryCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public HistoryCommand(CollectionManager collectionManager) {
        super("history", "history : вывести последние 11 команд (без их аргументов)");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) throws IOException {
        try {
            if (!(arg.length == 1)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.history();
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        }
    }
}
