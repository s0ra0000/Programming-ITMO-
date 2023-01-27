package run;

import utilities.Console;

import java.io.IOException;

/**
 * @author Davaanaym
 * @version 1.0.0
 * Console application that implements management of a collection of Study Group,
 * Main method
 */
public class App {
    public static void main(String[] args) throws IOException {
        if(args.length == 1){
            Console console = new Console(args);
            console.start();
        }
        else if(args.length == 0){
            System.out.println("Должно вводыть имя файла!");
        }
        else{
            System.out.println("Имя файла неправильно!");
        }
    }
}
