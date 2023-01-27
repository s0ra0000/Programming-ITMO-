package commands;

import exceptions.IncorrectCommandInputException;
import utilities.CollectionManager;
import utilities.CommandManager;

/**
 * Concrete command, Print all command with description
 */
public class HelpCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public HelpCommand(CollectionManager collectionManager) {
        super("help", "help : Вывести справку по доступным командом");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) {
        try {
            if (!(arg.length == 1)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.help();
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        }

    }
}
