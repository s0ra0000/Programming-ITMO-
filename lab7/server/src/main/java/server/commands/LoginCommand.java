package server.commands;
import common.interaction.ResponseCode;
import common.interaction.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;
import server.utilities.CollectionManager;
import server.utilities.DatabaseUserManager;
import server.utilities.ResponseOutputer;

import java.util.concurrent.locks.ReentrantLock;

public class LoginCommand extends AbstractCommand{
    public final Logger LOG
            = LoggerFactory.getLogger(Server.class);
    private final DatabaseUserManager databaseUserManager;
    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("login", "login : Входить в систему.");
        this.databaseUserManager = databaseUserManager;
    }
    @Override
    public void execute(String[] arg, User user, ReentrantLock reentrantLock){
        reentrantLock.lock();
        try{
            if(databaseUserManager.checkUserByUsernameAndPassword(user)){
                ResponseOutputer.append("Вы успешно вошли в систему.");
                DatabaseUserManager.responseCode = ResponseCode.OK;
                LOG.info("Пользователь " + user.getUsername()+ " вошел в систему.");
            }
            else{
                ResponseOutputer.append("Вы не можете войти в систему.");
                DatabaseUserManager.responseCode = ResponseCode.ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
        }

}
