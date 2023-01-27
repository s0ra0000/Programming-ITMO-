package server.commands;

import common.interaction.StudyGroupRaw;
import common.interaction.User;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Interface for all server.commands
 */
public interface ICommand {
    String getDescription();

    String getName();

    void execute(String[] arg, ReentrantLock reentrantLock) throws IOException;
    void execute(String[] arg, User user, ReentrantLock reentrantLock) throws IOException;
    void execute(String[] arg,StudyGroupRaw studyGroupRaw, User user, ReentrantLock reentrantLock);
}
