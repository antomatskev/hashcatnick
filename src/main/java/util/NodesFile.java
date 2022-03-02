package util;

import java.util.Map;

public class NodesFile {

    public String mainNodeIp() {
        Map<?, ?> read = (Map<?, ?>) new JsonFileContent("nodes.json").read().get("mainNode");
        return (String) read.get("ip");
    }

    public int mainNodePort() {
        Map<?, ?> read = (Map<?, ?>) new JsonFileContent("nodes.json").read().get("mainNode");
        return (int) read.get("port");
    }

}
