package utilities;

import commands.*;

import java.io.EOFException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Console. get commands from user
 */
public class Console {
    private final String path;

    /**
     * Constructor
     *
     * @param path Path of CSV file
     */
    public Console(String[] path) {
        this.path = path[0];
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        FileManager fileManager = new FileManager(path);
        QueryManager queryManager = new QueryManager(scanner);
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager(commandManager, queryManager, fileManager);

        commandManager.addCommand(new InsertCommand(collectionManager));
        commandManager.addCommand(new ShowCommand(collectionManager));
        commandManager.addCommand(new HelpCommand(collectionManager));
        commandManager.addCommand(new ClearCommand(collectionManager));
        commandManager.addCommand(new InfoCommand(collectionManager));
        commandManager.addCommand(new UpdateCommand(collectionManager));
        commandManager.addCommand(new RemoveCommand(collectionManager));
        commandManager.addCommand(new SaveCommand(collectionManager));
        commandManager.addCommand(new ExitCommand(collectionManager));
        commandManager.addCommand(new ExecuteScriptCommand(collectionManager));
        commandManager.addCommand(new HistoryCommand(collectionManager));
        commandManager.addCommand(new FilterContainsNameCommand(collectionManager));
        commandManager.addCommand(new FilterLessThanTransferredStudentsCommand(collectionManager));
        commandManager.addCommand(new RemoveLowerCommand(collectionManager));
        commandManager.addCommand(new ReplaceIfGreaterCommand(collectionManager));
        commandManager.addCommand(new FilterGreaterThanFormOfEducationCommand(collectionManager));

        System.out.print("$ ");
        Scanner sc = new Scanner(System.in);
        boolean repeat = sc.hasNextLine();
        do{
            try {
                commandManager.executeCommand(sc.nextLine().trim().split(" "));
                System.out.print("$ ");
            }
            catch(NoSuchElementException err){
                sc = new Scanner(System.in);
                repeat = sc.hasNextLine();
            }
        }
        while (repeat);
    }
}
