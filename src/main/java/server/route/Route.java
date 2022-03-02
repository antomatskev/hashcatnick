package server.route;

import com.sun.net.httpserver.HttpExchange;
import server.parse.Parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

public class Route {

    private final Parser parser;

    public Route(final Parser routeParser) {
        this.parser = routeParser;
    }

    public void routeResponse(HttpExchange exchange) throws IOException {
        final String response = this.parser.composeResponse(new String(exchange.getRequestBody().readAllBytes()));
        exchange.getResponseHeaders().put("Content-Type", Collections.singletonList("text/plain"));
        exchange.sendResponseHeaders(200, response.getBytes().length);
        final OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
