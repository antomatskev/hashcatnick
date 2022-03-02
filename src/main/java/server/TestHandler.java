package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.parse.GetParser;
import server.parse.PostParser;
import server.route.Route;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

class TestHandler implements HttpHandler {

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        switch (method) {
            case "GET": doGet(exchange); break;
            case "POST": doPost(exchange); break;
            default: doDefault(exchange); break;
        }
    }

    private void doGet(final HttpExchange exchange) throws IOException {
        final String response = exchange.getRequestURI().toString();
        exchange.getResponseHeaders().put("Content-Type", Collections.singletonList("text/plain"));
        exchange.sendResponseHeaders(200, response.getBytes().length);
        final OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        new Route(new GetParser(exchange.getRequestURI())).routeResponse(exchange);
    }

    private void doPost(final HttpExchange exchange) throws IOException {
        new Route(new PostParser(exchange.getRequestURI())).routeResponse(exchange);
    }

    private void doDefault(final HttpExchange exchange) throws IOException {
        final String response = "Das ist kleine test!";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        final OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
