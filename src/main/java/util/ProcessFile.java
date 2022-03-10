package util;

public class ProcessFile {

    private final String PROC_JSON = "proc.json";

    public String procJsonString() {
        return new JsonFileContent(PROC_JSON).readStringified();
    }

}
