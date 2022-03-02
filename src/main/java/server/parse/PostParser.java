package server.parse;

import java.io.IOException;
import java.net.URI;

public class PostParser implements Parser {

    private final URI uri;

    public PostParser(URI rUri) {
        this.uri = rUri;
    }

    @Override
    public String composeResponse(final String req) throws IOException {
        final String ret;
        switch (this.uri.getPath()) {
            case "/nodes":
                ret = req;
                break;
            default:
                ret = "=====\nUnknown request!\n====\n" + req;
                break;
        }
        return ret;
    }
}
