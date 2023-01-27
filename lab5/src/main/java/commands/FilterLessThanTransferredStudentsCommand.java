package commands;

import exceptions.IncorrectCommandInputException;
import utilities.CollectionManager;
import utilities.CommandManager;

import java.io.IOException;
/**
 * Concrete command, Number of transferred students
 */
public class FilterLessThanTransferredStudentsCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    public FilterLessThanTransferredStudentsCommand(CollectionManager collectionManager){
        super("filter_less_than_transferred_students","filter_less_than_transferred_students transferredStudents : вывести элементы, значение поля transferredStudents которых меньше заданного");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) throws IOException {
        try{
            if(!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.filter_less_than_transferred_students(arg[1]);
        }
        catch (IncorrectCommandInputException err){
            System.out.println(err.getMessage());;
            System.out.println("Использование: "+ getDescription());
        }
    }
}
