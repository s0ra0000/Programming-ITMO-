package utilities;
import commands.ICommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Invoker class
 */
public class CommandManager{
    private final static int MAX_HISTORY_SIZE = 11;
    private final HashMap<String,ICommand> commands = new HashMap<>();
    private static List<String> commandHistory = new ArrayList<>();

    /**
     * List of commands
     * @param command Concrete command
     */
    public void addCommand(ICommand command){
        commands.put(command.getName(),command);
    }

    /**
     * Execute concrete command by name
     * @param commandName Command name
     */
    public void executeCommand(String[] commandName){
        try {
            ICommand command = commands.get(commandName[0]);
            command.execute(commandName);
        }
        catch(NullPointerException | IOException err){
            System.out.println("Неверный комманд! help - вывести справку по доступным командам ");
        }
    }

    /**
     * Add commands to history
     * @param command command name
     */
    public static void addToHistory(String command){
        if (commandHistory.size() >= MAX_HISTORY_SIZE) {
            commandHistory.remove(0);
        }
        commandHistory.add(command);
    }

    /**
     * @return Command history list
     */
    public List<String> getCommandHistory(){
      return commandHistory;
    }

    /**
     * @return Commands list
     */
    public HashMap<String, ICommand> getCommands(){
        return commands;
    }
}
