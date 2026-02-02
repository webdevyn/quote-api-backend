import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class QuoteApp {
    //fetch quote from zenquotes site
    private static String fetchQuoteJson() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://zenquotes.io/api/random")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void main(String[] args) throws IOException {
        //create HTTP server (use localhost:8080 for now)
        HttpServer server = HttpServer.create(
            new InetSocketAddress("localhost", 8080), 0);
        //define the api endpoint
        server.createContext("/api/quote", exchange -> {
            try {
                // CORS headers FIRST
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "*");

                if ("OPTIONS".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(200, -1);
                    return;
                }
                //fetch the quote from the website using the function made above
                String jsonResponse = fetchQuoteJson();

                //set the json response headers
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
                exchange.sendResponseHeaders(200, jsonResponse.length());

                //send the json response
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(jsonResponse.getBytes());
                }
            } catch (Exception e) {
                exchange.sendResponseHeaders(500, -1);
            }
        });
        server.start();
        System.out.println("Server started at http://localhost:8080/api/quote . Press ENTER to stop.");
        System.in.read();
        server.stop(2);
        System.out.println("Server stopped.");

    }
}