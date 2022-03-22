package util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Collections;
import json.Node;
import java.util.List;

public class NodesFile {
    private static final String NODES_JSON = "nodes.json";
    
    private final Node mainNode;
    private final List<Node> nodes;
    
    @JsonCreator
    public NodesFile(@JsonProperty("mainNode") Node mainNode,
                     @JsonProperty("nodes") List<Node> nodes) {
        this.mainNode = mainNode;
        this.nodes = nodes;
    }
    
    public static NodesFile getInstance() {
        try {
            return new ObjectMapper().readValue(new File(NODES_JSON), NodesFile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new NodesFile(new Node("127.0.0.1", 8000, true), Collections.emptyList());
    }

    public String mainNodeIp() {
        return mainNode.getIp();
    }

    public int mainNodePort() {
        return mainNode.getPort();
    }

    public static String nodesJsonString() {
        try {
            return new ObjectMapper().writeValueAsString(getInstance());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "EMPTY";
    }
    
    public int lastUsedPort() {
        return nodes.isEmpty() ? 9000 : nodes.get(nodes.size() - 1).getPort();
    }

    public void updateMainNode(final String ip, final int port) {
        new JsonFileContent(NODES_JSON).writeMain(ip, port);
    }
    
    public Node getMainNode() {
        return mainNode;
    }
    
    public static void updateAddress(final String ip, final int port) {
        new JsonFileContent(NODES_JSON).write(ip, port);
    }
    
    public List<Node> getNodes() {
        return nodes;
    }
    
    public static void updateNodeStatus(String ip, int port) {
        new JsonFileContent(NODES_JSON).makeDead(ip, port);
    }
    
    public static void writeContent(String content) {
        new JsonFileContent(NODES_JSON).writeContent(content);
    }
    
    public static void clearNodesList() {
        NodesFile file = NodesFile.getInstance();
        file.getNodes().clear();
        try {
            new JsonFileContent(NODES_JSON).writeContent(new ObjectMapper().writeValueAsString(file));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
