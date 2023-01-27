package commands;

import exceptions.IncorrectCommandInputException;
import utilities.CollectionManager;
import utilities.CommandManager;

import java.io.IOException;

/**
 * Concrete command, Filter by name
 */
public class FilterContainsNameCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public FilterContainsNameCommand(CollectionManager collectionManager) {
        super("filter_contains_name", "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) throws IOException {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.filter_contains_name(arg[1]);
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        }
    }
}
