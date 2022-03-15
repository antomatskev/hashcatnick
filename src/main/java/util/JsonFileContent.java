package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import json.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		final Map<?, ?> currentContent = read();
		((List<Node>) currentContent.get("nodes")).add(new Node(ip, port, true));
		return getJsonStringContent(currentContent);
	}

	private String updatedMainContent(final String ip, final int port) {
		final Map<?, ?> currentContent = read();
		((Map<String, String>) currentContent.get("mainNode")).put("ip", ip);
		((Map<String, Integer>) currentContent.get("mainNode")).put("port", port);
		return getJsonStringContent(currentContent);
	}

	void makeDead(String ip, int port) {
		final Map<?, ?> currentContent = read();
		List<Node> nodes = ((List<Node>) currentContent.get("nodes"));
		for (Node node : nodes) {
			if (node.getIp().equals(ip) && node.getPort() == port) {
				node.setAlive(false);
			}
		}
		writeContent(getJsonStringContent(currentContent));

	}

	private String getJsonStringContent(Map<?, ?> currentContent) {
		final StringBuilder sb = new StringBuilder();
		try {
			sb.append(new ObjectMapper().writeValueAsString(currentContent));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
