package server.utilities;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AppServer;
import server.Server;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.*;

public class ConnectionHandler extends RecursiveAction {
    public final Logger LOG
            = LoggerFactory.getLogger(ConnectionHandler.class);
    private final Server server;
    private final Socket clientSocket;
    private final CommandManager commandManager;
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private final DatabaseUserManager databaseUserManager;
    public static Response finalResponseToUser;
    public static Request userRequest = null;
    public static Response responseToUser = null;
    public ConnectionHandler(Server server, Socket clientSocket,CommandManager commandManager,DatabaseUserManager databaseUserManager){
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
        this.databaseUserManager = databaseUserManager;

    }
    /**
     * The main computation performed by this task.
     *
     */
    @Override
    protected void compute() {

        boolean stopFlag = false;
        RequestHandler requestHandler;
        try(ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream());
        ){
            do{

                userRequest = (Request) clientReader.readObject();
                requestHandler = new RequestHandler(userRequest,commandManager,databaseUserManager);
                responseToUser = cachedThreadPool.submit(requestHandler).get();
                LOG.info("Запрос " + Arrays.toString(userRequest.getCommandName()) + " выполнен. Пользователь - " + userRequest.getUser().getUsername());
                finalResponseToUser = responseToUser;
                if(!forkJoinPool.submit(() -> {
                    try{
                        clientWriter.writeObject(finalResponseToUser);
                        clientWriter.flush();
                        return true;
                    } catch (IOException e) {
                        LOG.error(e.getMessage());
                    }
                    return false;
                }).get()) break;

            }while(responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT);
            //
            if(responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT){
                stopFlag = true;
            }
        } catch (IOException | ClassNotFoundException | InterruptedException | ExecutionException e) {
            LOG.error(e.getMessage());
        }
        finally {
            try {
                forkJoinPool.shutdown();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(stopFlag) {
                try {
                    server.stop();
                    server.releaseConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
