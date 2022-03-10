package server.parse;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface Parser {

    String composeResponse(final HttpExchange input) throws IOException;

}
