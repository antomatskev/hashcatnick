package server.parse;

import java.io.IOException;

public interface Parser {

    String composeResponse(final String input) throws IOException;

}
