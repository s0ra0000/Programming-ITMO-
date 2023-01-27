package client.utilities;

import java.util.Scanner;

public class Login {
    private final Scanner scanner;
    public Login(Scanner scanner){
        this.scanner = scanner;
    }
    public String askUsername(){
        String username;
        System.out.print("Username: ");
        username = scanner.nextLine().trim();
        return username;
    }

    public String askPassword(){
        String password;
        System.out.print("Password: ");
        password = scanner.nextLine().trim();
        return password;
    }

}
