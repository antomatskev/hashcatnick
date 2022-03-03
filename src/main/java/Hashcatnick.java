import client.Client;
import server.Server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Hashcatnick {

    public static void main(String[] args) {
        System.out.println("===STARTING HYDRATOR===");
        try {
            final boolean isMainNode = args.length != 0 && Objects.equals(args[0], "main");
            new Client().start(isMainNode);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}