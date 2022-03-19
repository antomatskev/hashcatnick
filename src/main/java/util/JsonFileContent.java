package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import json.Node;

public class JsonFileContent {

	private final String NO_DATA = "No data found!";

	private final String fileName;

	public JsonFileContent(final String name) {
		this.fileName = name;
	}

	protected Map<?, ?> read() {
		try {
			return new ObjectMapper().readValue(Paths.get(fileName).toFile(), Map.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new HashMap<>();
	}

	protected void write(final String ip, final int port) {
		final String content = updatedContent(ip, port);
		writeContent(content);
	}

	protected void writeMain(final String ip, final int port) {
		final String content = updatedMainContent(ip, port);
		writeContent(content);
	}

	public void writeContent(String content) {
		try (FileWriter fw = new FileWriter(fileName)) {
			fw.write(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected String readStringified() {
		final StringBuilder ret = new StringBuilder();
		try {
			Map<?, ?> read = read();
			ret.append(new ObjectMapper().writeValueAsString(read.isEmpty() ? NO_DATA : read));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret.toString();
	}

	private String updatedContent(final String ip, final int port) {
		NodesFile nodesFile = NodesFile.getInstance();
		nodesFile.getNodes().add(new Node(ip, port, true));
		return getJsonStringContent(nodesFile);
	}

	private String updatedMainContent(final String ip, final int port) {
		NodesFile nodesFile = NodesFile.getInstance();
		nodesFile.getMainNode().setIp(ip);
		nodesFile.getMainNode().setPort(port);
		return getJsonStringContent(nodesFile);
	}

	void makeDead(String ip, int port) {
		NodesFile nodesFile = NodesFile.getInstance();
		for (Node node : nodesFile.getNodes()) {
			if (node.getIp().equals(ip) && node.getPort() == port) {
				node.setAlive(false);
			}
		}
		writeContent(getJsonStringContent(nodesFile));

	}

	private String getJsonStringContent(Object currentContent) {
		final StringBuilder sb = new StringBuilder();
		try {
			sb.append(new ObjectMapper().writeValueAsString(currentContent));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
