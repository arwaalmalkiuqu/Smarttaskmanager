package taskmanager.impl;

import taskmanager.api.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Default implementation of the TaskManager facade.
 * It connects task storage, weather fetching, and schedule planning.
 */
public class DefaultTaskManager implements TaskManager {

    // Service that stores and manages task objects.
    private final DefaultTaskService taskService;

    // Service that fetches weather data.
    private final WeatherService weatherService;

    // Planner that creates weather-based task recommendations.
    private final SchedulePlanner schedulePlanner;

    /**
     * 1- Precondition: The builder provides an API key and optional storage path.
     * 2- Postcondition: The task, weather, and planner services are initialized.
     * 3- Exceptions: Runtime errors may occur if a service cannot be created.
     * 4- @param apiKey The weather API key.
     * 4- @param storagePath Optional path for future task storage.
     * 5- @throws RuntimeException if service creation fails unexpectedly.
     * 6- @return No direct return value because constructors create objects.
     *
     * @param apiKey The weather API key.
     * @param storagePath Optional path for future task storage.
     */
    DefaultTaskManager(String apiKey, String storagePath) {
        this.weatherService = new WeatherService(apiKey);
        this.taskService = new DefaultTaskService();
        this.schedulePlanner = new DefaultSchedulePlanner(weatherService);
    }

    /**
     * 1- Precondition: The task is not null and contains valid data.
     * 2- Postcondition: The task is added to the task service.
     * 3- Exceptions: InvalidTaskException is thrown if the task is invalid.
     * 4- @param task The task to add.
     * 5- @throws InvalidTaskException if the task data is invalid.
     * 6- @return No return value because this method is void.
     *
     * @param task The task to add.
     * @throws InvalidTaskException if the task data is invalid.
     */
    @Override
    public void addTask(Task task) {
        // The Mono is blocked here to keep the facade simple and synchronous.
        taskService.addTask(task).block();
    }

    /**
     * 1- Precondition: The taskId belongs to an existing task.
     * 2- Postcondition: The task is removed from the task service.
     * 3- Exceptions: TaskNotFoundException is thrown if the task does not exist.
     * 4- @param taskId The ID of the task to remove.
     * 5- @throws TaskNotFoundException if no task matches the ID.
     * 6- @return No return value because this method is void.
     *
     * @param taskId The ID of the task to remove.
     * @throws TaskNotFoundException if no task matches the ID.
     */
    @Override
    public void removeTask(String taskId) {
        taskService.removeTask(taskId).block();
    }

    /**
     * 1- Precondition: The task service is initialized.
     * 2- Postcondition: All current tasks are returned as a list.
     * 3- Exceptions: Runtime errors may occur if task loading fails.
     * 4- @param None.
     * 5- @throws RuntimeException if task loading fails unexpectedly.
     * 6- @return A list of all tasks.
     *
     * @return A list of all tasks.
     */
    @Override
    public List<Task> getTasks() {
        // The Mono is blocked here to return a normal Java List.
        return taskService.findAllTasksAsList().block();
    }

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
    @Override
    public Mono<WeatherForecast> fetchWeather(String location) {
        return weatherService.fetchWeather(location);
    }

    /**
     * 1- Precondition: The schedule planner is initialized.
     * 2- Postcondition: The schedule planner is returned.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The schedule planner object.
     *
     * @return The schedule planner object.
     */
    @Override
    public SchedulePlanner getPlanner() {
        return schedulePlanner;
    }

    /**
     * 1- Precondition: The task service is initialized.
     * 2- Postcondition: The internal task service is returned for UI use.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The internal DefaultTaskService.
     *
     * @return The internal DefaultTaskService.
     */
    public DefaultTaskService getTaskService() {
        return taskService;
    }
}
