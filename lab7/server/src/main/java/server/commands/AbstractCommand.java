package server.commands;

import common.interaction.StudyGroupRaw;
import common.interaction.User;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract Command class contains Object methods, name and description.
 */
public abstract class AbstractCommand implements ICommand {
    private String name;
    private String description;

    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void execute(String[] arg, ReentrantLock reentrantLock) throws IOException {
    }
    public void execute(String[] arg, User user,ReentrantLock reentrantLock) throws IOException {
    }
    public void execute(String[] arg, StudyGroupRaw studyGroupRaw,User user, ReentrantLock reentrantLock){
    }

}
