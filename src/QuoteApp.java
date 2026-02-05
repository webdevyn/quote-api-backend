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
    //fetch quotes from type.fit (free quote sites seem to go down often so check this regularly)
    private static String fetchQuoteJson() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://type.fit/api/quotes"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // the api returns all available quotes
        // need to parse and pick one random quote
        String allQuotes = response.body();

        // use Gson to parse the JSON array
        com.google.gson.Gson gson = new com.google.gson.Gson();
        com.google.gson.JsonArray quotesArray = gson.fromJson(allQuotes, com.google.gson.JsonArray.class);

        // pick a random quote
        int randomIndex = new java.util.Random().nextInt(quotesArray.size());
        com.google.gson.JsonObject randomQuote = quotesArray.get(randomIndex).getAsJsonObject();

        // return just the one random quote as JSON
        return gson.toJson(randomQuote);
    }

    public static void main(String[] args) throws IOException {
        //create HTTP server (use localhost:8080 for now)
        HttpServer server = HttpServer.create(
            new InetSocketAddress("0.0.0.0", 8080), 0);
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
        System.out.println("Server started on port 8080 at /api/quote");

        // keep JVM alive and server running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}