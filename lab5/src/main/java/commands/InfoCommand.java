package commands;

import exceptions.IncorrectCommandInputException;
import utilities.CollectionManager;
import utilities.CommandManager;

/**
 * Concrete command, Print information about collection
 */
public class InfoCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "info : Вывести в стандартный поток вывода информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) {
        try {
            if (!(arg.length == 1)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.info();
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        }
    }
}
