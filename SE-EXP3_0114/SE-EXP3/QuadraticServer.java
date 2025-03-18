import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class QuadraticServer {

    public static void main(String[] args) throws IOException {
        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/solveQuadratic", new QuadraticHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:8080/solveQuadratic");
    }

    static class QuadraticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }

            // Get query parameters
            String query = exchange.getRequestURI().getQuery();
            Map<String, Double> params = parseQuery(query);

            // Ensure valid parameters are provided
            if (!params.containsKey("a") || !params.containsKey("b") || !params.containsKey("c")) {
                String error = "Error: Please provide valid query parameters (a, b, c)";
                exchange.sendResponseHeaders(400, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
                return;
            }

            double a = params.get("a");
            double b = params.get("b");
            double c = params.get("c");

            String response = solveQuadratic(a, b, c);

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        // ✅ Fixed query parameter parsing (removed .stream() issues)
        private Map<String, Double> parseQuery(String query) {
            Map<String, Double> paramMap = new HashMap<>();
            if (query == null || query.isEmpty())
                return paramMap;

            String[] params = query.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    try {
                        paramMap.put(pair[0], Double.parseDouble(pair[1]));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format: " + param);
                    }
                }
            }
            return paramMap;
        }

        private String solveQuadratic(double a, double b, double c) {
            if (a == 0)
                return "Error: 'a' cannot be zero (not a quadratic equation)";

            double determinant = b * b - 4 * a * c;

            if (determinant > 0) {
                double root1 = (-b + Math.sqrt(determinant)) / (2 * a);
                double root2 = (-b - Math.sqrt(determinant)) / (2 * a);
                return "Roots are: " + root1 + " and " + root2;
            } else if (determinant == 0) {
                double root = -b / (2 * a);
                return "Root is: " + root;
            } else {
                return "No Real Solutions (Complex Roots)";
            }
        }
    }
}
