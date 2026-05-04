package taskmanager.api;

import reactor.core.publisher.Mono;
import java.util.List;

/**
 * Defines the smart scheduling operations that use weather data.
 */
public interface SchedulePlanner {

    /**
     * 1- Precondition: The task list and forecast are available.
     * 2- Postcondition: A recommendation is generated for each task.
     * 3- Exceptions: Runtime errors may be emitted through the returned Mono if processing fails.
     * 4- @param tasks The tasks to evaluate.
     * 4- @param forecast The weather forecast used for evaluation.
     * 5- @throws RuntimeException if schedule generation fails unexpectedly.
     * 6- @return A Mono that emits a list of schedule recommendations.
     *
     * @param tasks The tasks to evaluate.
     * @param forecast The weather forecast used for evaluation.
     * @return A Mono that emits a list of schedule recommendations.
     */
    Mono<List<ScheduleRecommendation>> suggestSchedule(
            List<Task> tasks,
            WeatherForecast forecast);

    /**
     * 1- Precondition: The task list exists and the location name is provided.
     * 2- Postcondition: Weather is fetched and recommendations are generated.
     * 3- Exceptions: A WeatherAPIException may be emitted if weather fetching fails.
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
    Mono<List<ScheduleRecommendation>> suggestScheduleForLocation(
            List<Task> tasks,
            String location);
}
