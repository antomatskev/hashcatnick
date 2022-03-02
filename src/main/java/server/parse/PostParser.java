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
            case "/json-to-xml":
                ret = req;
                break;
            case "/json-to-soap":
                ret = req + "\n======\nPretty SOAPy story!";
                break;
            default:
                ret = req + " \n=====\nThat's it, folks!";
                break;
        }
        return ret;
    }
}
