package server.commands;

import common.exceptions.IncorrectCommandInputException;
import common.interaction.User;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concrete command, Clear collection
 */
public class ClearCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "clear : Очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg, User user, ReentrantLock reentrantLock) throws IOException {
        reentrantLock.lock();
        try {
            if (!(arg.length == 1)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.clear(user);
        } catch (IncorrectCommandInputException | SQLException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        }finally {
            reentrantLock.unlock();
        }
    }
}
