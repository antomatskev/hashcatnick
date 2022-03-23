package server.parse;

import com.sun.net.httpserver.HttpExchange;
import util.NodesFile;
import util.ProcessFile;

import java.net.URI;

public class GetParser implements Parser {

    private final URI uri;

    public GetParser(URI rUri) {
        this.uri = rUri;
    }

    @Override
    public String composeResponse(final HttpExchange req) {
        final String ret;
        switch (this.uri.getPath()) {
            case "/nodes":
                ret = NodesFile.nodesJsonString();
                break;
            case "/process":
                ret = new ProcessFile().procJsonString();
                break;
            default:
                ret = "=====\nUnknown request!\n====\n" + req.getRequestURI().toString();
                break;
        }
        return ret;
    }

}
