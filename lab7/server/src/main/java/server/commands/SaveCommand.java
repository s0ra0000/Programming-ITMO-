package server.commands;

import common.exceptions.IncorrectCommandInputException;
import common.interaction.User;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Concrete command, Save collection to file
 */
public class SaveCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public SaveCommand(CollectionManager collectionManager) {
        super("save", "save : Сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
    }


    @Override
    public void execute(String[] arg, User user, ReentrantLock reentrantLock) {
        reentrantLock.unlock();
        try {
            if (!(arg.length == 1)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            //collectionManager.save();
        } catch (IncorrectCommandInputException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        }finally {
            reentrantLock.unlock();
        }

    }
}
