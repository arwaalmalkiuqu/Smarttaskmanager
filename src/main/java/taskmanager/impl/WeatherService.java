package taskmanager.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import taskmanager.api.WeatherAPIException;
import taskmanager.api.WeatherForecast;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for fetching real-time weather data
 * from the OpenWeatherMap API.
 *
 * <p>Caches results for 10 minutes to avoid unnecessary API calls.
 * All HTTP operations run on a background thread using Project Reactor.
 */
public class WeatherService {

    // Base URL for current weather data
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    // Cache duration: 10 minutes in milliseconds
    private static final long CACHE_EXPIRY_MILLIS = 10 * 60 * 1000L;

    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper jsonMapper;

    // ConcurrentHashMap is thread-safe so multiple threads won't corrupt cache data
    private final Map<String, CacheEntry> weatherCache = new ConcurrentHashMap<>();

    /**
     * Constructs a WeatherService with the given API key.
     *
     * @param apiKey Your OpenWeatherMap API key
     */
    public WeatherService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient(); // Reuse the same HttpClient for all requests
        this.jsonMapper = new ObjectMapper(); // JSON to Java object mapper
    }

    /**
     * Fetches a weather forecast for the given city.
     *
     * <p>If a fresh (< 10 min old) cached result exists, it is returned immediately
     * without making an API call.
     *
     * @param city City name (e.g., "Jeddah")
     * @return Mono emitting a WeatherForecast, or error on failure.
     * @throws WeatherAPIException if the HTTP call or JSON parsing fails.
     */
    public Mono<WeatherForecast> fetchWeather(String city) {
        return Mono.fromCallable(() -> {

            // Check the cache first for a valid entry before making an API call
            CacheEntry cached = weatherCache.get(city.toLowerCase());
            if (cached != null && !cached.isExpired()) {
                System.out.println("[WeatherService] Cache hit for: " + city);
                return cached.forecast;
            }

            // Build the API URL
            String url = BASE_URL + "?q=" + city + "&appid=" + apiKey + "&units=metric";

            System.out.println("[WeatherService] Fetching weather from API for: " + city);

            // Send HTTP GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Execute the request and get the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Turn JSON response into WeatherForecast object
            WeatherForecast forecast = parseResponse(city, response.body());

            // Store in cache
            weatherCache.put(city.toLowerCase(), new CacheEntry(forecast));

            return forecast;

        }).subscribeOn(Schedulers.boundedElastic()) // Run HTTP call on background thread
          .onErrorMap(e -> {
              // Wrap any error into our custom WeatherAPIException
              if (e instanceof WeatherAPIException) return e;
              return new WeatherAPIException("Failed to fetch weather for " + city, e);
          });
    }

    /**
     * Parses the JSON body from OpenWeatherMap into a WeatherForecast.
     *
     * @param city     The city name (used as label).
     * @param jsonBody The raw JSON string from the API.
     * @return A populated WeatherForecast.
     * @throws WeatherAPIException if the JSON cannot be parsed.
     */
    private WeatherForecast parseResponse(String city, String jsonBody) {
        try {
            // Parse the JSON response into a tree structure for easy navigation
            JsonNode root = jsonMapper.readTree(jsonBody);

            // Check for API errors
            if (root.has("cod") && root.get("cod").asInt() != 200) {
                String msg = root.has("message")
                        ? root.get("message").asText()
                        : "Unknown error";
                throw new WeatherAPIException("Weather API error: " + msg, null);
            }

            // Extract temperature in Celsius
            double temp = root.path("main").path("temp").asDouble();

            // Weather condition description (e.g., "clear sky", "light rain")
            String condition = root.path("weather")
                                   .get(0)
                                   .path("description")
                                   .asText("unknown");

            // Extract rain probability from clouds percentage
            double rainProb = root.path("clouds").path("all").asDouble(0) / 100.0;

            // If there is rain data, increase the probability
            if (root.has("rain")) {
                rainProb = Math.max(rainProb, 0.8);
            }

            return new WeatherForecast(city, LocalDateTime.now(), temp, condition, rainProb);

        } catch (WeatherAPIException e) {
            throw e;
        } catch (Exception e) {
            throw new WeatherAPIException("Could not parse weather response for " + city, e);
        }
    }

    /**
     * Holds a cached forecast plus the timestamp when it was fetched.
     */
    private static class CacheEntry {

        final WeatherForecast forecast;
        final long fetchedAtMillis;

        /**
         * Creates a new CacheEntry with the current timestamp.
         *
         * @param forecast The weather forecast to cache.
         */
        CacheEntry(WeatherForecast forecast) {
            this.forecast = forecast;
            this.fetchedAtMillis = System.currentTimeMillis();
        }

        /**
         * Checks if this cache entry has expired.
         *
         * @return True if the cache entry is older than the expiry time.
         */
        boolean isExpired() {
            return (System.currentTimeMillis() - fetchedAtMillis) > CACHE_EXPIRY_MILLIS;
        }
    }
}