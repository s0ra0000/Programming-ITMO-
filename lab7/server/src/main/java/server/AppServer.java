package server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import common.exceptions.CannotReadFileException;
import org.postgresql.*;
import server.commands.*;
import server.utilities.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class AppServer {
    public static final int PORT = 4004;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;
    public final Logger LOG
            = LoggerFactory.getLogger(AppServer.class);
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        if(args.length == 3){
            Class.forName("org.postgresql.Driver");
            String databaseHost = args[1];
            String databasePassword = args[2];
            String databaseAddress = "jdbc:postgresql://" + databaseHost + ":5432/studs";
            final String user = "s291011";

            DatabaseHandler databaseHandler = new DatabaseHandler(databaseAddress,user, databasePassword);
            databaseHandler.connectToDatabase();
            ReentrantLock reentrantLock = new ReentrantLock();
            CommandManager commandManager = new CommandManager(reentrantLock);
            DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
            DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler,databaseUserManager);
            CollectionManager collectionManager = new CollectionManager(commandManager,databaseCollectionManager);
            commandManager.addCommand(new ShowCommand(collectionManager));
            commandManager.addCommand(new InsertCommand(collectionManager));
            commandManager.addCommand(new HelpCommand(collectionManager));
            commandManager.addCommand(new HistoryCommand(collectionManager));
            commandManager.addCommand(new ClearCommand(collectionManager));
            commandManager.addCommand(new FilterContainsNameCommand(collectionManager));
            commandManager.addCommand(new FilterGreaterThanFormOfEducationCommand(collectionManager));
            commandManager.addCommand(new FilterLessThanTransferredStudentsCommand(collectionManager));
            commandManager.addCommand(new InfoCommand(collectionManager));
            commandManager.addCommand(new RemoveCommand(collectionManager));
            commandManager.addCommand(new UpdateCommand(collectionManager));
            commandManager.addCommand(new ReplaceIfGreaterCommand(collectionManager));
            commandManager.addCommand(new ExecuteScriptCommand(collectionManager));
            commandManager.addCommand(new ExitCommand(collectionManager));
            commandManager.addCommand(new LoginCommand(databaseUserManager));
            commandManager.addCommand(new RegisterCommand(databaseUserManager));
            Server server = new Server(PORT,CONNECTION_TIMEOUT,collectionManager,commandManager,databaseUserManager);
            server.run();
            databaseHandler.closeConnection();
        }
        else if(args.length == 0) System.out.println("Должно вводыть имя файла!");

        else System.out.println("Имя файла неправильно!");
    }
}
