package common.interaction;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String[] commandName;
    private final Serializable commandObjectArgument;
    private final User user;
    public Request(String[] commandName,Serializable commandObjectArgument,User user){
        this.commandName = commandName;
        this.commandObjectArgument = commandObjectArgument;
        this.user = user;
    }
    public Request(String[] commandName,User user){
        this(commandName,null,user);
    }
    public User getUser() {
        return user;
    }
    public String[] getCommandName(){
        return commandName;
    }
    public Object getCommandObjectArgument(){
        return commandObjectArgument;
    }
//    public boolean isEmpty(){
//        return commandName.isEmpty() && commandObjectArgument == null;
//    }
    @Override
    public String toString(){
        return "Request[" + Arrays.toString(commandName) + ", " + commandObjectArgument + "]";
    }

    public boolean isEmpty() {
        return commandName[0].isEmpty() && commandObjectArgument == null;
   }
}
