package machine;

import java.util.Scanner;

public class CoffeeMachine {
    static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        InputHandler.execute(null);
        while (true) {
            InputHandler.execute(scanner.nextLine());
        }
    }
}
