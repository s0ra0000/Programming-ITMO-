package server.commands;

import common.exceptions.IncorrectCommandInputException;
import common.interaction.User;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concrete command, Filter by form of education
 */
public class FilterGreaterThanFormOfEducationCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public FilterGreaterThanFormOfEducationCommand(CollectionManager collectionManager) {
        super("filter_greater_than_form_of_education", "filter_greater_than_form_of_education formOfEducation : вывести элементы, значение поля formOfEducation которых больше заданного");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg, User user, ReentrantLock reentrantLock) throws IOException {
        reentrantLock.lock();
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.filter_greater_than_form_of_education(arg[1]);
        } catch (IncorrectCommandInputException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        }finally {
            reentrantLock.unlock();
        }
    }
}
