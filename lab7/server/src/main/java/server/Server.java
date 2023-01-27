package server;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.interaction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utilities.*;

public class Server {
    private final int port;
    private final int soTimeout;
    private ServerSocket serverSocket;
    private boolean clientSocketStatus = false;
    private boolean serverExitStatus = false;
    private Scanner scanner = new Scanner(System.in);
    private boolean isStopped;
    private Semaphore semaphore;
    private ForkJoinPool forkJoinPool = new ForkJoinPool();
    private CommandManager commandManager;
    private DatabaseUserManager databaseUserManager;
    CollectionManager collectionManager;
    String command ="";
    public final Logger LOG
            = LoggerFactory.getLogger(Server.class);
    public Server(int port, int soTimeout, CollectionManager collectionManager, CommandManager commandManager, DatabaseUserManager databaseUserManager){
        this.port = port;
        this.soTimeout = soTimeout;
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
        this.databaseUserManager = databaseUserManager;
        this.semaphore = new Semaphore(1000);
    }


    public void run() throws IOException {
        try{
            openServerSocket();
            Runnable userInput = () ->{
                while(true){
                    try{
                        String[] userCommand = scanner.nextLine().trim().split(" ");

                        if(userCommand.length == 1 && userCommand[0].equals("exit_server")){
                            try {
                                    stop();
                                    break;

                            } catch (IOException e) { e.printStackTrace(); }
                        }
                        //else if(userCommand[0].equals("save"))  collectionManager.save();
                        else LOG.error("Нет таких команд!\nДля завершения сервера - exit_server\nДля сохранение коллекцию в файл - save");
                    }catch (NoSuchElementException err){
                        LOG.error("Непредвиденная ошибка!");
                        System.exit(0);
                    }

                }
            };
            Thread thread = new Thread(userInput);
            thread.start();
            while(!isStopped()){
                try{
                    acquireConnection();
                    if(isStopped()) throw new ConnectException();
                    Socket clientSocket = connectToClient();
                    forkJoinPool.execute(new ConnectionHandler(this,clientSocket,commandManager,databaseUserManager));

                } catch (ConnectionErrorException | SocketTimeoutException exception) {
                    clientSocketStatus = false;
                    break;
                } catch (IOException err){
                    clientSocketStatus = false;
                    LOG.error("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
        } catch(OpeningServerSocketException err){
            LOG.error("Сервер не может быть запущен!");
        }
    }
    private synchronized boolean isStopped() {
        return isStopped;
    }

    public void openServerSocket() throws IOException {
        try{
            LOG.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            LOG.info("Сервер успешно запущен.");
        } catch(IllegalArgumentException err){
            LOG.error("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        }
        catch (IOException err){
            LOG.error("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    public Socket connectToClient() throws IOException {
        try{
            LOG.info("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            LOG.info("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch(SocketTimeoutException err){
            LOG.error("Превышено время ожидания подключения!");
            throw new SocketTimeoutException();
        }
        catch (IOException err) {
            LOG.error("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }



    public void acquireConnection() {
        try {
            semaphore.acquire();
            LOG.info("Разрешение на новое соединение получено.");
        } catch (InterruptedException exception) {
            ResponseOutputer.append("Произошла ошибка при получении разрешения на новое соединение!");
            LOG.error("Произошла ошибка при получении разрешения на новое соединение!");
        }
    }
    public void releaseConnection() {
        semaphore.release();
        LOG.info("Разрыв соединения зарегистрирован.");
    }
    public void stop() throws IOException {
        try{
            LOG.info("Завершение работы сервера...");
            if(serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            forkJoinPool.shutdown();
            serverSocket.close();
            LOG.info("Работа сервера успешно завершена.");
            System.exit(0);
        }catch (ClosingSocketException err){
            LOG.error("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException err){
            LOG.error("Произошла ошибка при завершении работы сервера!");
        }
    }

}
