package server.parse;

import java.net.URI;

public class GetParser implements Parser {

    private final URI uri;

    public GetParser(URI rUri) {
        this.uri = rUri;
    }

    @Override
    public String composeResponse(final String input) {
        return this.uri.getPath();
    }

}
