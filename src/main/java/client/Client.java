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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Client {
    private static final String ADDRESS_FORMAT = "%s:%d";

    private final String mainNodeIp = NodesFile.getInstance().mainNodeIp();
    private final int mainNodePort = NodesFile.getInstance().mainNodePort();
    private final AtomicInteger port = new AtomicInteger();
    private Server server;

    public void start(final boolean isMainNode, final int prt) throws IOException {
        System.out.println("===CLIENT STARTED===");
        port.set(prt);
        server = new Server(this);
        server.start(isMainNode);
        if (!isMainNode) {
            composeGetRequest(String.format(ADDRESS_FORMAT, mainNodeIp, mainNodePort), "nodes");
        }
        mainLoop();
    }

    private void mainLoop() {
        final Scanner scanner = new Scanner(System.in);
        final AtomicBoolean isExit = new AtomicBoolean();
        do {
            processInput(scanner, isExit);
        } while (!isExit.get());
        scanner.close();
    }

    private void processInput(Scanner scanner, AtomicBoolean isExit) {
        final String input = scanner.nextLine();
        final String firstWord = input.split(" ")[0];
        switch (firstWord) {
            case "exit":
                System.out.println("====FINISHING CLIENT====");
                isExit.set(true);
                server.closeNodeServer();
                break;
            case "nodes":
                composeGetRequest(String.format(ADDRESS_FORMAT, mainNodeIp, mainNodePort), "nodes");
                break;
            case "process":
                composeGetRequest(String.format(ADDRESS_FORMAT, mainNodeIp, mainNodePort), "process");
                break;
            case "hashcat":
                composePostRequest(String.format(ADDRESS_FORMAT, mainNodeIp, mainNodePort), "process", input);
                break;
            default:
                System.out.println("Unknown command: " + input);
                break;
        }
    }

    public void sendAddressUpdate() {
        try {
            final URL url = new URL("http://" + String.format(ADDRESS_FORMAT, mainNodeIp, mainNodePort) + "/nodes");
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            byte[] out = NodesFile.nodesJsonString().getBytes(StandardCharsets.UTF_8);
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

    private void composeGetRequest(String ip, final String endpoint) {
        try {
            final URL url = new URL("http://" + ip + "/" + endpoint);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            List<String> response = continueConnection(con);
            if (response != null && endpoint.equals("nodes")) {
                NodesFile.writeContent(computeResponseNodes(response));
            }
        } catch (ConnectException ce) {
            System.out.println(ce.getMessage() + ". Enter 'exit' to finish the program.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String computeResponseNodes(List<String> response) {
        int findIndex = IntStream.range(0, response.size()).
                filter(row -> response.get(row).isEmpty()).
                findFirst().orElse(-1);
        return IntStream.range(findIndex + 1, response.size()).
                mapToObj(response::get).
                collect(Collectors.joining("\n"));
    }
    
    private void composePostRequest(String ip, final String endpoint, final String command) {
        try {
            final URL url = new URL("http://" + ip + "/" + endpoint);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = command.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
            }
            continueConnection(con);
        } catch (ConnectException ce) {
            System.out.println(ce.getMessage() + ". Enter 'exit' to finish the program.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> continueConnection(HttpURLConnection con) throws IOException {
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setInstanceFollowRedirects(false);
        int status = con.getResponseCode();
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        List<String> content = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            content.add(inputLine);
        }
        if (status > 299) {
            System.out.println("Something went wrong: " + String.join("\n", content));
        } else {
            System.out.println(String.join("\n", content));
        }
        in.close();
        con.disconnect();
        return status > 299 ? null : content;
    }

    public int determinePort() {
        if (port.get() <= 0) {
            port.set(NodesFile.getInstance().lastUsedPort() + 1);
        }
        return port.get();
    }

}
