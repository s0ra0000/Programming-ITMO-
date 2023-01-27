package server.utilities;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.interaction.User;

import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Response> {
    CommandManager commandManager;
    private boolean serverExitStatus;
    User hashUser = new User();
    User user  = new User();
    private DatabaseUserManager databaseUserManager;
    private Request request;
    public RequestHandler(Request request, CommandManager commandManager,DatabaseUserManager databaseUserManager){
        this.commandManager = commandManager;
        this.databaseUserManager = databaseUserManager;
        this.request = request;
    }
//    public Response handle(Request request){
//        ResponseCode responseCode = null;
//        if((request.getCommandName()[0].equals("exit_server"))){
//            commandManager.executeCommand(request.getCommandName(),request.getCommandObjectArgument(),request.getUser());
//            responseCode = ResponseCode.SERVER_EXIT;
//        } else if (request.getCommandName()[0].equals("login") || request.getCommandName()[0].equals("register")) {
//            commandManager.executeCommand(request.getCommandName(), request.getCommandObjectArgument(),request.getUser());
//            responseCode = DatabaseUserManager.responseCode;
//        } else if (commandManager.executeCommand(request.getCommandName(), request.getCommandObjectArgument(),request.getUser())) {
//            responseCode = ResponseCode.OK;
//        } else {
//            responseCode = ResponseCode.ERROR;
//        }
//        return new Response(responseCode,ResponseOutputer.getOutPut());
//    }

//    @Override
//    public void run() {
//
//    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Response call() throws Exception {
            user = request.getUser();
         ResponseCode responseCode = null;
        if((request.getCommandName()[0].equals("exit_server"))){
            commandManager.executeCommand(request.getCommandName(),request.getCommandObjectArgument(),user);
            responseCode = ResponseCode.SERVER_EXIT;
        } else if (request.getCommandName()[0].equals("login") || request.getCommandName()[0].equals("register")) {
            hashUser.setUsername(request.getUser().getUsername());
            hashUser.setPassword(PasswordHasher.hashPassword(request.getUser().getPassword()));
            commandManager.executeCommand(request.getCommandName(), null,hashUser);
            responseCode = DatabaseUserManager.responseCode;
        } else if (commandManager.executeCommand(request.getCommandName(), request.getCommandObjectArgument(),user)) {
            responseCode = ResponseCode.OK;
        } else {
            responseCode = ResponseCode.ERROR;
        }
        return new Response(responseCode,ResponseOutputer.getOutPut());
    }
}
