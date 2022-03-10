package server.parse;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class PostParser implements Parser {

    private final URI uri;

    public PostParser(URI rUri) {
        this.uri = rUri;
    }

    @Override
    public String composeResponse(final HttpExchange req) throws IOException {
        final String ret;
        switch (this.uri.getPath()) {
            case "/nodes":
                ret = req.getRequestURI().toString();
                break;
            case "/process":
                ret = readReqBody(req);
                break;
            default:
                ret = "=====\nUnknown request!\n====\n" + req.getRequestURI().toString();
                break;
        }
        return ret;
    }

    private String readReqBody(final HttpExchange exchange) {
        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            int b;
            StringBuilder buf = new StringBuilder("Received command: ");
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            return buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Something went wrong!";
    }

}
