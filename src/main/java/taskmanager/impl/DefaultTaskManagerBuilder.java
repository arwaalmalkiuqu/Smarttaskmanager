package taskmanager.impl;

import taskmanager.api.TaskManager;

/**
 * Builder implementation used to create a configured TaskManager object.
 */
public class DefaultTaskManagerBuilder implements TaskManager.TaskManagerBuilder {

    // Weather API key used by WeatherService.
    private String apiKey = "";

    // Optional file path for future task saving/loading.
    private String storagePath = null;

    /**
     * 1- Precondition: The caller provides an API key string.
     * 2- Postcondition: The API key value is stored in this builder.
     * 3- Exceptions: No exception is expected.
     * 4- @param apiKey The weather API key.
     * 5- @throws None.
     * 6- @return This builder for method chaining.
     *
     * @param apiKey The weather API key.
     * @return This builder for method chaining.
     */
    @Override
    public TaskManager.TaskManagerBuilder withWeatherApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * 1- Precondition: The caller provides a storage path string.
     * 2- Postcondition: The storage path value is stored in this builder.
     * 3- Exceptions: No exception is expected.
     * 4- @param path The optional storage path.
     * 5- @throws None.
     * 6- @return This builder for method chaining.
     *
     * @param path The optional storage path.
     * @return This builder for method chaining.
     */
    @Override
    public TaskManager.TaskManagerBuilder withStoragePath(String path) {
        this.storagePath = path;
        return this;
    }

    /**
     * 1- Precondition: Builder settings are ready.
     * 2- Postcondition: A DefaultTaskManager object is created.
     * 3- Exceptions: Runtime errors may occur if object creation fails.
     * 4- @param None.
     * 5- @throws RuntimeException if manager creation fails unexpectedly.
     * 6- @return A ready-to-use TaskManager.
     *
     * @return A ready-to-use TaskManager.
     */
    @Override
    public TaskManager build() {
        // New manager uses the saved API key and storage path.
        return new DefaultTaskManager(apiKey, storagePath);
    }
}
