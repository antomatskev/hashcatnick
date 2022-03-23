package executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {
    
    private CommandExecutor() {
    }
    
    public static String executeCommand(String command) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (isWindows) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("sh", "-c", command);
        }
        processBuilder.directory(new File(System.getProperty("user.home")));
        try {
            Process process = processBuilder.start();
            List<String> result = new ArrayList<>();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = r.readLine();
                while (line != null) {
                    result.add(line);
                    line = r.readLine();
                }
            }
            return String.join("\n", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
