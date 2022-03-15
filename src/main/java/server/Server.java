package server;

import client.Client;
import com.sun.net.httpserver.HttpServer;
import server.handler.MainHandler;
import util.NodesFile;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {

    private final Client client;
    private final String ip;
    private final int port;

    public Server(Client client) {
        this.client = client;
        ip = getLocalIp();
        port = client.determinePort();
    }

    public void start(final boolean isMainNode) throws IOException {
        System.out.println("===SERVER STARTED===");
        final NodesFile nodeFile = new NodesFile();
        final HttpServer server = isMainNode
                ? startMainNodeServer(nodeFile)
                : startNodeServer(nodeFile);
        server.createContext("/", new MainHandler());
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
    
    public void closeNodeServer() {
        new NodesFile().updateNodeStatus(ip, port);
        client.sendAddressUpdate();
    }

    private HttpServer startMainNodeServer(final NodesFile mainNode) throws IOException {
        final String mainNodeIp = mainNode.mainNodeIp();
        final int mainNodePort = client.determinePort();
        mainNode.updateMainNode(mainNodeIp, mainNodePort);
        return HttpServer.create(
                new InetSocketAddress(mainNodeIp, mainNodePort), 0);
    }

    private HttpServer startNodeServer(final NodesFile nodeFile) throws IOException {
        writeAndSendOwnAddress(ip, port);
        return HttpServer.create(
                new InetSocketAddress(ip, port), 0);
    }
    
    private void writeAndSendOwnAddress(final String ip, final int port) {
        new NodesFile().updateAddress(ip, port);
        client.sendAddressUpdate();
    }
    
    private String getLocalIp() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return  "";
        }
    }

}
