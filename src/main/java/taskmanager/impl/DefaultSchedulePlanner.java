package taskmanager.impl;

import taskmanager.api.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Default implementation of SchedulePlanner.
 * It creates weather-aware suggestions for each task.
 */
public class DefaultSchedulePlanner implements SchedulePlanner {

    // Service used to get weather data from the API.
    private final WeatherService weatherService;

    /**
     * 1- Precondition: A WeatherService object is provided.
     * 2- Postcondition: A planner object is created and connected to the weather service.
     * 3- Exceptions: No exception is expected in this constructor.
     * 4- @param weatherService The service used to fetch weather data.
     * 5- @throws None directly.
     * 6- @return No direct return value because constructors create objects.
     *
     * @param weatherService The service used to fetch weather data.
     */
    public DefaultSchedulePlanner(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * 1- Precondition: The task list and forecast are available.
     * 2- Postcondition: Each task receives one schedule recommendation.
     * 3- Exceptions: Runtime errors may be emitted if tasks or forecast are invalid.
     * 4- @param tasks The tasks to evaluate.
     * 4- @param forecast The weather forecast used for the decision.
     * 5- @throws RuntimeException if recommendation generation fails unexpectedly.
     * 6- @return A Mono that emits a list of schedule recommendations.
     *
     * @param tasks The tasks to evaluate.
     * @param forecast The weather forecast used for the decision.
     * @return A Mono that emits a list of schedule recommendations.
     */
    @Override
    public Mono<List<ScheduleRecommendation>> suggestSchedule(List<Task> tasks, WeatherForecast forecast) {
        return Flux.fromIterable(tasks)
                .map(task -> buildRecommendation(task, forecast))  // task: one task from the list.
                .collectList();                                    // list: all recommendations together.
    }

    /**
     * 1- Precondition: The task list exists and the location name is provided.
     * 2- Postcondition: Weather is fetched, then recommendations are created.
     * 3- Exceptions: WeatherAPIException may be emitted if the weather request fails.
     * 4- @param tasks The tasks to evaluate.
     * 4- @param location The city name used to fetch weather.
     * 5- @throws WeatherAPIException if the weather API request fails.
     * 6- @return A Mono that emits a list of schedule recommendations.
     *
     * @param tasks The tasks to evaluate.
     * @param location The city name used to fetch weather.
     * @return A Mono that emits a list of schedule recommendations.
     * @throws WeatherAPIException if the weather API request fails.
     */
    @Override
    public Mono<List<ScheduleRecommendation>> suggestScheduleForLocation(List<Task> tasks, String location) {
        return weatherService.fetchWeather(location)                 // forecast: weather for the selected city.
                .flatMap(forecast -> suggestSchedule(tasks, forecast));
    }

    /**
     * 1- Precondition: The task and forecast are available.
     * 2- Postcondition: One recommendation object is created for the task.
     * 3- Exceptions: Runtime errors may occur if task or forecast is null.
     * 4- @param task The task being checked.
     * 4- @param forecast The weather data used for checking.
     * 5- @throws RuntimeException if task or forecast data cannot be read.
     * 6- @return A ScheduleRecommendation containing the task and message.
     *
     * @param task The task being checked.
     * @param forecast The weather data used for checking.
     * @return A ScheduleRecommendation containing the task and message.
     */
    private ScheduleRecommendation buildRecommendation(Task task, WeatherForecast forecast) {
        // If weather does not affect the task, it is automatically safe.
        if (!task.isWeatherSensitive()) {
            return new ScheduleRecommendation(task, "✅ SAFE – task is not weather-sensitive.");
        }

        // Temperature value used to detect extreme heat.
        if (forecast.getTemperatureCelsius() > 40.0) {
            return new ScheduleRecommendation(task,
                    "🔴 RISKY – Extreme heat (" + forecast.getTemperatureCelsius() + "°C). Consider rescheduling.");
        }

        // Rain percentage shown in the recommendation message.
        if (forecast.getPrecipitationProbability() > 0.6) {
            int rainPct = (int) (forecast.getPrecipitationProbability() * 100);
            return new ScheduleRecommendation(task,
                    "🌧 RISKY – " + rainPct + "% chance of rain in " + forecast.getLocation() + ". Consider rescheduling.");
        }

        // Default message when the weather is acceptable.
        return new ScheduleRecommendation(task,
                "✅ SAFE – Weather looks good in " + forecast.getLocation() +
                " (" + forecast.getCondition() + ", " + forecast.getTemperatureCelsius() + "°C).");
    }
}
