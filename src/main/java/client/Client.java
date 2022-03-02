package client;

import server.Server;
import util.NodesFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Client {

    private final String mainNode = new NodesFile().mainNodeIp() + ":" + new NodesFile().mainNodePort();

    public void start(final boolean isMainNode) throws IOException {
        System.out.println("===CLIENT STARTED===");
        if (!isMainNode) {
            new Server().start(isMainNode);
            askForKnownNodes();
        }
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            final String input = scanner.nextLine();
            if (input.equals("exit")) {
                System.out.println("====FINISHING CLIENT====");
                break;
            } else if (input.equals("nodes")) {
                askForKnownNodes();
            }
        }
        scanner.close();
    }

    private void askForKnownNodes() {
        try {
            final URL url = new URL("http://" + mainNode + "/nodes");
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
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
            System.out.println(content);
            in.close();
            Reader streamReader = null;

            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
