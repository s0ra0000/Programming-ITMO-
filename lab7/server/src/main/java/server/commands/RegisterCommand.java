package server.commands;
import common.interaction.ResponseCode;
import common.interaction.User;
import server.utilities.CollectionManager;
import server.utilities.DatabaseUserManager;
import server.utilities.ResponseOutputer;

import java.util.concurrent.locks.ReentrantLock;

public class RegisterCommand extends AbstractCommand{
    private final DatabaseUserManager databaseUserManager;
    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register", "register : зарегистрировать новый пользователь.");
        this.databaseUserManager = databaseUserManager;
    }
    @Override
    public void execute(String[] arg, User user, ReentrantLock reentrantLock){
        reentrantLock.lock();
        try{
            if(databaseUserManager.insertUser(user)){
                ResponseOutputer.append("Пользователь" + user.getUsername() + " зарегистрирован.");
                DatabaseUserManager.responseCode = ResponseCode.REGISTERED;
                System.out.println("done");
            }
            else{
                ResponseOutputer.append(user.getUsername() + "Имя пользователя существует.");
                DatabaseUserManager.responseCode = ResponseCode.ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

}
