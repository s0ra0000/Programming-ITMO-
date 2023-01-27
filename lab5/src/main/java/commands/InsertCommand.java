package commands;

import exceptions.IncorrectCommandInputException;
import exceptions.IncorrectInputScriptException;
import utilities.CollectionManager;
import utilities.CommandManager;

/**
 * Concrete command, Insert new element
 */
public class InsertCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public InsertCommand(CollectionManager collectionManager) {
        super("insert", "insert null {element} : Добавить новый элемент с заданным ключом");
        this.collectionManager = collectionManager;

    }

    @Override
    public void execute(String[] arg) {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.insert(arg[1]);
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        } catch (IncorrectInputScriptException err) {
            throw new IncorrectInputScriptException();
        }
    }
}
