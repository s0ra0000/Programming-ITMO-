package client.utilities;

import client.AppClient;
import common.interaction.Request;
import common.interaction.User;

import java.util.Scanner;

public class AuthenticationHandler {
    private Scanner scanner;
    private int authType;
    private String[] command;
    private final int LOGINCOMMAND = 1;
    private final int REGISTERCOMMAND = 2;
    public AuthenticationHandler(Scanner scanner){
        this.scanner = scanner;
    }
    public Request handler(){
        AppClient.user = new User();
        authAsk();
        return new Request(command,AppClient.user);
    }

    public void authAsk(){
        Login login = new Login(scanner);
        Register register = new Register(scanner);

        while(true){
        try{
            System.out.println("1. LOGIN");
            System.out.println("2. REGISTER");
            System.out.print("> ");
                authType = Integer.parseInt(scanner.nextLine().trim());
                if(authType == LOGINCOMMAND){
//                    user = new User(login.askUsername(),login.askPassword());
                    AppClient.user.setUsername(login.askUsername());
                    AppClient.user.setPassword(login.askPassword());
                    command = "login".split(" ");
                    break;
                }
                else if(authType == REGISTERCOMMAND){
//                    user = new User(register.askNewUsername(),register.askNewPassword());
                    AppClient.user.setUsername(register.askNewUsername());
                    AppClient.user.setPassword(register.askNewPassword());
                    command = "register".split(" ");;
                    break;
                }
                else{
                    throw new NumberFormatException();
                }
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
        catch (NumberFormatException e){
            System.out.println("Должно выводить 1 или 2.");
        }}
    }
}
