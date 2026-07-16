# Quote API Backend

A lightweight Java REST API that serves random quotes from the type.fit API. This backend provides a simple `/api/quote` endpoint that fetches and returns a randomly selected quote in JSON format. Built with Java 17 and the Java HTTP Server.

## Overview

This is a minimalist backend service designed to provide dynamic quote data for frontend applications. It fetches quotes from an external API, randomly selects one, and exposes it through a RESTful endpoint with full CORS support.

## Features

- **Random Quote Generation** - Fetches quotes from type.fit and returns a random selection
- **RESTful API** - Simple GET endpoint for quote retrieval
- **CORS Enabled** - Supports cross-origin requests from any domain
- **Lightweight** - No heavy frameworks; uses Java's built-in HTTP server
- **Containerized** - Multi-stage Docker build for minimal image size
- **Production Ready** - Can be deployed standalone or as part of a larger application

## Tech Stack

- **Language**: Java 17
- **HTTP Server**: Java built-in `com.sun.net.httpserver`
- **JSON Processing**: Gson 2.10.1
- **Build Tool**: Maven 3.9
- **Containerization**: Docker with Alpine Linux
- **External API**: type.fit (free quotes API)

## Project Structure

```
quote-api-backend/
├── src/
│   └── QuoteApp.java              # Main application with HTTP server and quote fetching logic
├── pom.xml                        # Maven configuration with dependencies and build settings
├── Dockerfile                     # Multi-stage Docker build configuration
├── portfolio-quote-app.iml        # IntelliJ project file
└── README.md                      # This file
```

## Dependencies

- **Gson** (2.10.1) - Google's JSON library for parsing and serializing JSON

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.9 or higher (for building)
- Docker (for containerized deployment)

### Installation & Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/webdevyn/quote-api-backend.git
   cd quote-api-backend
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/quote-api-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

   The server will start on `http://localhost:8080`

## API Endpoints

### Get Random Quote

**Request:**
```
GET /api/quote
```

**Response:**
```json
{
  "text": "The only way to do great work is to love what you do.",
  "author": "Steve Jobs"
}
```

**Status Codes:**
- `200 OK` - Successfully returned a random quote
- `500 Internal Server Error` - Error fetching or processing quotes

### CORS Support

The API supports Cross-Origin Resource Sharing (CORS) for requests from any origin:
- **Access-Control-Allow-Origin**: `*`
- **Access-Control-Allow-Methods**: `GET, OPTIONS`
- **Access-Control-Allow-Headers**: `*`

## Docker Deployment

The project includes a multi-stage Dockerfile that builds and packages the application efficiently.

### Build Docker Image

```bash
docker build -t quote-api:latest .
```

### Run Docker Container

```bash
docker run -p 8080:8080 quote-api:latest
```

The API will be available at `http://localhost:8080/api/quote`

## Build Configuration

### Maven Build

The `pom.xml` includes:
- **Java 17** compilation and target
- **Gson** dependency for JSON processing
- **Maven Assembly Plugin** - Creates a fat JAR with all dependencies
- **Maven Compiler Plugin** - Ensures proper source directory configuration

Build commands:
```bash
# Clean build
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run locally
mvn exec:java -Dexec.mainClass="QuoteApp"
```

### Docker Build

The Dockerfile uses a **multi-stage build**:

1. **Build Stage** - Maven builds the application
2. **Runtime Stage** - Minimal Alpine Linux JRE container

This approach significantly reduces the final image size by excluding build tools from the production image.

## How It Works

1. Client makes a GET request to `/api/quote`
2. Server receives the request and calls `fetchQuoteJson()`
3. `fetchQuoteJson()` makes an HTTP request to the type.fit API
4. The full quotes array is parsed using Gson
5. A random quote is selected from the array
6. The random quote is returned as JSON to the client

## Configuration

The application is configured to:
- Listen on `0.0.0.0:8080` (all interfaces, port 8080)
- Handle CORS requests automatically
- Support both GET and OPTIONS HTTP methods
- Return JSON responses with UTF-8 encoding

## Error Handling

- **External API Failure**: Returns HTTP 500 if the type.fit API is unavailable
- **JSON Parsing Errors**: Returns HTTP 500 if quote data is malformed
- **Network Issues**: Returns HTTP 500 if connection fails

## Performance Considerations

- **Stateless Design** - Each request independently fetches a quote (no caching)
- **Async HTTP** - Uses Java's modern HTTP client for efficient requests
- **Minimal Dependencies** - Only requires Gson for external dependencies

## Integration

This backend is designed to work with frontend applications. Example integration:

```javascript
// React example
const fetchQuote = async () => {
  const response = await fetch('http://localhost:8080/api/quote');
  const quote = await response.json();
  console.log(quote.text, '-', quote.author);
};
```

## Future Enhancements

- Add caching to reduce external API calls
- Implement rate limiting
- Add quote filtering by author
- Support multiple quote sources
- Add request logging and monitoring
- Implement health check endpoint

## Troubleshooting

### Port Already in Use
If port 8080 is already in use:
```bash
# Change the port in QuoteApp.java (line 39)
# Or use Docker with a different port mapping:
docker run -p 9000:8080 quote-api:latest
```

### type.fit API Unavailable
The external quote API may occasionally be down. Check:
- https://type.fit/api/quotes

If the service is unavailable, consider adding a fallback quote or alternative API source.

### Build Issues
Ensure Maven configuration recognizes the non-standard source directory:
```bash
# The pom.xml includes <sourceDirectory>${project.basedir}/src</sourceDirectory>
# to tell Maven to look for sources in /src instead of /src/main/java/
```

## License

This project is open source and available under the MIT License.

## Contact

For issues or questions, visit [@webdevyn](https://github.com/webdevyn) on GitHub.
