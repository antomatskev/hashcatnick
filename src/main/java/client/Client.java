package client;

import server.Server;
import util.NodesFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    private final String mainNode = new NodesFile().mainNodeIp() + ":" + new NodesFile().mainNodePort();
    private final AtomicInteger port = new AtomicInteger();

    public void start(final boolean isMainNode, final int prt) throws IOException {
        System.out.println("===CLIENT STARTED===");
        port.set(prt);
        new Server(this).start(isMainNode);
        if (!isMainNode) {
            composeGetRequest("nodes");
        }
        final Scanner scanner = new Scanner(System.in);
        final AtomicBoolean isExit = new AtomicBoolean();
        do {
            final String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    System.out.println("====FINISHING CLIENT====");
                    isExit.set(true);
                    break;
                case "nodes":
                    composeGetRequest("nodes");
                    break;
                case "process":
                    composeGetRequest("process");
                    break;
                default:
                    System.out.println("Unknown command: " + input);
                    break;
            }
        } while (!isExit.get());
        scanner.close();
    }

    public void sendAddressUpdate() {
        try {
            final URL url = new URL("http://" + mainNode + "/nodes");
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            byte[] out = new NodesFile().nodesJsonString().getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(out.length);
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                os.write(out);
            }
            continueConnection(con);
        } catch (ConnectException ce) {
            System.out.println(ce.getMessage() + ". Enter 'exit' to finish the program.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void composeGetRequest(final String endpoint) {
        try {
            final URL url = new URL("http://" + mainNode + "/" + endpoint);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            continueConnection(con);
        } catch (ConnectException ce) {
            System.out.println(ce.getMessage() + ". Enter 'exit' to finish the program.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void continueConnection(HttpURLConnection con) throws IOException {
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setInstanceFollowRedirects(false);
        int status = con.getResponseCode();
        final BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        if (status > 299) {
            System.out.println("Something went wrong: " + content);
        } else {
            System.out.println(content);
        }
        in.close();
        con.disconnect();
    }

    public int determinePort() {
        if (port.get() <= 0) {
            port.set(new NodesFile().lastUsedPort() + 1);
        }
        return port.get();
    }

}
