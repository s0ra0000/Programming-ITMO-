package server.commands;

import common.exceptions.IncorrectCommandInputException;
import common.interaction.User;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concrete command, Remove by key
 */
public class RemoveCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveCommand(CollectionManager collectionManager) {
        super("remove_key", "remove_key null : удалить элемент из коллекции по его ключу");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg, User user, ReentrantLock reentrantLock) {
        reentrantLock.lock();
        try {
            System.out.println(arg.length);
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.remove(arg[1],user);
        } catch (IncorrectCommandInputException | SQLException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        } finally {
            reentrantLock.unlock();
        }
    }
}
