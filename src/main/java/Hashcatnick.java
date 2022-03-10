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
            if (args.length == 0) {
                new Client().start(false, -1);
            } else {
                startClientWithArgs(args);
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startClientWithArgs(final String[] args) throws IOException {
        boolean isMainNode;
        int port;
        final String arg = args[0];
        if (args.length == 1) {
            try {
                port = Integer.parseInt(arg);
                isMainNode = false;
            } catch (NumberFormatException nfe) {
                port = -1;
                isMainNode = "main".equals(arg);
            }
        } else if (args.length == 2) {
            port = Integer.parseInt(arg);
            isMainNode = "main".equals(args[1]);
        } else {
            isMainNode = false;
            port = -1;
        }
        new Client().start(isMainNode, port);
    }

}