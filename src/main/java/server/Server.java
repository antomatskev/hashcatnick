package server;

import com.sun.net.httpserver.HttpServer;
import util.NodesFile;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {

    public void start(final boolean isMainNode) throws IOException {
        System.out.println("===SERVER STARTED===");
        final NodesFile nodeFile = new NodesFile();
        final HttpServer server = isMainNode
                ? startMainNodeServer(nodeFile)
                : startNodeServer(nodeFile);
        server.createContext("/", new TestHandler());
        server.setExecutor(null);
        final String startMsg = "===STARTING NODE: "
                + (isMainNode
                ? "main node "
                : "node ")
                + "on address "
                + server.getAddress()
                + "===";
        System.out.println(startMsg);
        server.start();
    }

    private HttpServer startMainNodeServer(final NodesFile mainNode) throws IOException {
        return HttpServer.create(
                new InetSocketAddress(mainNode.mainNodeIp(), mainNode.mainNodePort()), 0);
    }

    private HttpServer startNodeServer(final NodesFile nodeFile) throws IOException {
        String ip;
        int port;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
            port = 9001;
        } catch (Exception e) {
            e.printStackTrace();
            ip = "";
            port = 0;
        }
        // TODO: write own IP and port to nodes.json and send them to the main node.
//        return HttpServer.create(
//                new InetSocketAddress(nodeFile.writeNodeIp(ip), nodeFile.writeNodePort(port)), 0);
        return HttpServer.create(
                new InetSocketAddress(ip, port), 0);
    }

}
