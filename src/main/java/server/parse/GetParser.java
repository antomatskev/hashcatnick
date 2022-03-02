package server.parse;

import util.NodesFile;

import java.net.URI;

public class GetParser implements Parser {

    private final URI uri;

    public GetParser(URI rUri) {
        this.uri = rUri;
    }

    @Override
    public String composeResponse(final String req) {
        final String ret;
        switch (this.uri.getPath()) {
            case "/nodes":
                ret = new NodesFile().nodesJsonString();
                break;
            default:
                ret = "=====\nUnknown request!\n====\n" + req;
                break;
        }
        return ret;
    }

}
