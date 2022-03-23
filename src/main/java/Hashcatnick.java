import client.Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Hashcatnick {
    private static final String DEFAULT_NODES =
            "{\n" +
                    "  \"mainNode\": {\n" +
                    "    \"ip\": \"127.0.0.1\",\n" +
                    "    \"port\": 8000,\n" +
                    "    \"isAlive\": true\n" +
                    "  },\n" +
                    "  \"nodes\": [\n" +
                    "  ]\n" +
                    "}";
    private static final String DEFAULT_PROC = "{\n" +
            "\n" +
            "}";
    
    public static void main(String[] args) throws IOException {
        createDefaultFiles();
        System.out.println("===STARTING HASHCATNIK===");
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
    
    private static void createDefaultFiles() throws IOException {
        File nodes = new File("nodes.json");
        if (nodes.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(nodes, false)) {
                fos.write(DEFAULT_NODES.getBytes(StandardCharsets.UTF_8));
                fos.flush();
            }
        }
    
        File process = new File("proc.json");
        if (nodes.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(process, false)) {
                fos.write(DEFAULT_PROC.getBytes(StandardCharsets.UTF_8));
                fos.flush();
            }
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