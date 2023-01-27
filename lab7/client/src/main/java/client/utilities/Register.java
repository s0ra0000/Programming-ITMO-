package client.utilities;

import java.util.Scanner;

public class Register {
    private Scanner scanner;
    private String password;
    public Register(Scanner scanner){
        this.scanner = scanner;
    }
    public String askNewUsername(){
        String username;
        System.out.print("Username: ");
        username = scanner.nextLine().trim();
        return username;
    }
    public String askNewPassword(){
        while(true){
            password = askPassword();
            if(password.equals(askRepeatPassword())){
                return password;
            }
        }
    }
    public String askPassword(){
        String password;
        System.out.print("Password: ");
        password = scanner.nextLine().trim();
        return password;
    }

    public String askRepeatPassword(){
        String password;
        System.out.print("Repeat password: ");
        password = scanner.nextLine().trim();
        return password;
    }
}
