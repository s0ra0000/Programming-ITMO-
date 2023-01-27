package server.utilities;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import common.interaction.StudyGroupRaw;
import common.interaction.User;
import server.commands.ICommand;
import server.commands.InsertCommand;
import server.commands.UpdateCommand;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Invoker class
 */
public class CommandManager{
    private final static int MAX_HISTORY_SIZE = 11;
    private final HashMap<String,ICommand> commands = new HashMap<>();
    private static final List<String> commandHistory = new ArrayList<>();
    private final ReentrantLock reentrantLock;
    public CommandManager(ReentrantLock reentrantLock){
        this.reentrantLock = reentrantLock;
    }
    /**
     * List of server.commands
     * @param command Concrete command
     */
    public void addCommand(ICommand command){
        commands.put(command.getName(),command);
    }

    /**
     * Execute concrete command by name
     * @param commandName Command name
     */
    public boolean executeCommand(String[] commandName, Object commandObjectArgument, User user){
        try {
            ICommand command = commands.get(commandName[0]);
            if(commandObjectArgument == null){
                command.execute(commandName, user,reentrantLock);

            }
            else{
                command.execute(commandName,(StudyGroupRaw) commandObjectArgument, user,reentrantLock);
            }
            return true;
        }
        catch(NullPointerException | IOException err){
            ResponseOutputer.append("Неверный комманд! help - вывести справку по доступным командам ");
            return false;
        }
    }

    /**
     * Add server.commands to history
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
