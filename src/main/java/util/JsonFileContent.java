package util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JsonFileContent {

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

    protected String readStringified() {
        final StringBuilder ret = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(l -> ret.append(l.trim()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}
