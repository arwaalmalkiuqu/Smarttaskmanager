package taskmanager.api;

import reactor.core.publisher.Mono;
import taskmanager.impl.DefaultTaskManagerBuilder;

import java.util.List;

/**
 * Main entry point for the Smart Task Manager system.
 */
public interface TaskManager {

    /**
     * 1- Precondition: The task is not null and contains valid data.
     * 2- Postcondition: The task is added to the system.
     * 3- Exceptions: InvalidTaskException is thrown if the task is invalid.
     * 4- @param task The task to add.
     * 5- @throws InvalidTaskException if the task data is invalid.
     * 6- @return No return value because this method is void.
     *
     * @param task The task to add.
     * @throws InvalidTaskException if the task data is invalid.
     */
    void addTask(Task task);

    /**
     * 1- Precondition: The taskId belongs to an existing task.
     * 2- Postcondition: The task is removed from the system.
     * 3- Exceptions: TaskNotFoundException is thrown if the task is not found.
     * 4- @param taskId The ID of the task to remove.
     * 5- @throws TaskNotFoundException if no task matches the ID.
     * 6- @return No return value because this method is void.
     *
     * @param taskId The ID of the task to remove.
     * @throws TaskNotFoundException if no task matches the ID.
     */
    void removeTask(String taskId);

    /**
     * 1- Precondition: The task manager is initialized.
     * 2- Postcondition: The current tasks are returned without being changed.
     * 3- Exceptions: Runtime errors may occur if the task service fails.
     * 4- @param None.
     * 5- @throws RuntimeException if loading tasks fails unexpectedly.
     * 6- @return A list of all current tasks.
     *
     * @return A list of all current tasks.
     */
    List<Task> getTasks();

    /**
     * 1- Precondition: A valid location name is provided.
     * 2- Postcondition: A weather request is started asynchronously.
     * 3- Exceptions: WeatherAPIException may be emitted through the returned Mono.
     * 4- @param location The city name used for weather lookup.
     * 5- @throws WeatherAPIException if the weather request fails.
     * 6- @return A Mono that emits the weather forecast.
     *
     * @param location The city name used for weather lookup.
     * @return A Mono that emits the weather forecast.
     * @throws WeatherAPIException if the weather request fails.
     */
    Mono<WeatherForecast> fetchWeather(String location);

    /**
     * 1- Precondition: The task manager is initialized.
     * 2- Postcondition: The schedule planner is returned.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The planner used for schedule suggestions.
     *
     * @return The planner used for schedule suggestions.
     */
    SchedulePlanner getPlanner();

    /**
     * 1- Precondition: The builder implementation class is available.
     * 2- Postcondition: A new builder object is created.
     * 3- Exceptions: Runtime errors may occur if the builder cannot be created.
     * 4- @param None.
     * 5- @throws RuntimeException if builder creation fails unexpectedly.
     * 6- @return A new TaskManagerBuilder.
     *
     * @return A new TaskManagerBuilder.
     */
    static TaskManagerBuilder builder() {
        // Builder object used to configure and create a TaskManager.
        return new DefaultTaskManagerBuilder();
    }

    /**
     * Builder interface used to configure and create a TaskManager.
     */
    interface TaskManagerBuilder {

        /**
         * 1- Precondition: The caller provides an API key string.
         * 2- Postcondition: The API key is saved in the builder.
         * 3- Exceptions: No exception is expected in the interface definition.
         * 4- @param apiKey The OpenWeatherMap API key.
         * 5- @throws None.
         * 6- @return This builder for method chaining.
         *
         * @param apiKey The OpenWeatherMap API key.
         * @return This builder for method chaining.
         */
        TaskManagerBuilder withWeatherApiKey(String apiKey);

        /**
         * 1- Precondition: The caller provides a storage path string.
         * 2- Postcondition: The storage path is saved in the builder.
         * 3- Exceptions: No exception is expected in the interface definition.
         * 4- @param path The file path for task storage.
         * 5- @throws None.
         * 6- @return This builder for method chaining.
         *
         * @param path The file path for task storage.
         * @return This builder for method chaining.
         */
        TaskManagerBuilder withStoragePath(String path);

        /**
         * 1- Precondition: Required builder settings are ready.
         * 2- Postcondition: A configured TaskManager is created.
         * 3- Exceptions: Runtime errors may occur if construction fails.
         * 4- @param None.
         * 5- @throws RuntimeException if creating the manager fails unexpectedly.
         * 6- @return A ready-to-use TaskManager.
         *
         * @return A ready-to-use TaskManager.
         */
        TaskManager build();
    }
}
