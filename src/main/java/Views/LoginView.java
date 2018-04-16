package Views;

import java.util.Scanner;
import java.io.Console;

public class LoginView{

    private Scanner reader = new Scanner(System.in);

    public void displayText(String text){
        System.out.println(text);
    }

    public String getPassword(){
        Console console = System.console();
        char passwordArray[] = console.readPassword("Enter password: ");
        String password = String.valueOf(passwordArray);
        return password;
    }

    public String getLogin(){
        System.out.println("Enter login: ");
        String login = reader.nextLine();
        return login;
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
