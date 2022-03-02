package client;

import server.Server;

import java.io.IOException;
import java.util.Scanner;

public class Client {


    public void start(final boolean isMainNode) throws IOException {
        System.out.println("===CLIENT STARTED===");
        new Server().start(isMainNode);
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            final String input = scanner.nextLine();
            if (input.equals("exit")) {
                System.out.println("====FINISHING CLIENT====");
                break;
            }
        }
        scanner.close();
    }

}
