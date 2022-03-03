package util;

import json.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NodesFile {

    private final String NODES_JSON = "nodes.json";

    public String mainNodeIp() {
        Map<?, ?> read = (Map<?, ?>) new JsonFileContent(NODES_JSON).read().get("mainNode");
        return (String) read.get("ip");
    }

    public int mainNodePort() {
        Map<?, ?> read = (Map<?, ?>) new JsonFileContent(NODES_JSON).read().get("mainNode");
        return (int) read.get("port");
    }

    public String nodesJsonString() {
        return new JsonFileContent(NODES_JSON).readStringified();
    }

    public void updateAddress(final String ip, final int port) {
        new JsonFileContent(NODES_JSON).write(ip, port);
    }

    public int lastUsedPort() {
        List<Node> nodes = (ArrayList<Node>) new JsonFileContent(NODES_JSON).read().get("nodes");
        return nodes.isEmpty() ? 9000 : (int) ((Map<?, ?>) nodes.get(nodes.size() - 1)).get("port");
    }

}
