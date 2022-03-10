package server.route;

import com.sun.net.httpserver.HttpExchange;
import server.parse.Parser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class Route {

    private final Parser parser;

    public Route(final Parser routeParser) {
        this.parser = routeParser;
    }

    public void routeResponse(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        final String response = this.parser.composeResponse(exchange);
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream responseStream = exchange.getResponseBody()) {
            responseStream.write(response.getBytes(Charset.defaultCharset()));
        }
    }
}
