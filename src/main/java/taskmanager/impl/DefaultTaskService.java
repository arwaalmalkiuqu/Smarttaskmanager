package taskmanager.impl;

import taskmanager.api.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default in-memory implementation of TaskService.
 */
public class DefaultTaskService implements TaskService {

    // In-memory storage where key = task ID and value = Task object.
    private final Map<String, Task> taskStore = new ConcurrentHashMap<>();

    /**
     * 1- Precondition: The task is not null and has a non-empty title and due date.
     * 2- Postcondition: The task is saved in the taskStore map.
     * 3- Exceptions: InvalidTaskException is emitted if the task data is invalid.
     * 4- @param task The task to add or update.
     * 5- @throws InvalidTaskException if the task is null or missing required data.
     * 6- @return A Mono that completes when the task is saved.
     *
     * @param task The task to add or update.
     * @return A Mono that completes when the task is saved.
     * @throws InvalidTaskException if the task is null or missing required data.
     */
    @Override
    public Mono<Void> addTask(Task task) {
        return Mono.fromRunnable(() -> {
            // task: object that will be checked before saving.
            if (task == null) {
                throw new InvalidTaskException("Task must not be null.");
            }

            // title: short name required for showing the task to the user.
            if (task.getTitle() == null || task.getTitle().isBlank()) {
                throw new InvalidTaskException("Task title must not be empty.");
            }

            // dueDateTime: required date/time for scheduling the task.
            if (task.getDueDateTime() == null) {
                throw new InvalidTaskException("Task due date must not be null.");
            }

            // Store or replace the task using its ID.
            taskStore.put(task.getId(), task);
        });
    }

    /**
     * 1- Precondition: The taskId belongs to an existing task.
     * 2- Postcondition: The task is removed from the taskStore map.
     * 3- Exceptions: TaskNotFoundException is emitted if the ID is not found.
     * 4- @param taskId The ID of the task to remove.
     * 5- @throws TaskNotFoundException if no task matches the ID.
     * 6- @return A Mono that completes when the task is removed.
     *
     * @param taskId The ID of the task to remove.
     * @return A Mono that completes when the task is removed.
     * @throws TaskNotFoundException if no task matches the ID.
     */
    @Override
    public Mono<Void> removeTask(String taskId) {
        return Mono.fromRunnable(() -> {
            // taskId: key used to find the task inside the map.
            if (!taskStore.containsKey(taskId)) {
                throw new TaskNotFoundException(taskId);
            }

            // Remove the selected task from storage.
            taskStore.remove(taskId);
        });
    }

    /**
     * 1- Precondition: The taskId is provided.
     * 2- Postcondition: The matching task is returned if found.
     * 3- Exceptions: TaskNotFoundException is emitted if the ID is not found.
     * 4- @param taskId The ID used to search for the task.
     * 5- @throws TaskNotFoundException if no task matches the ID.
     * 6- @return A Mono that emits the found Task.
     *
     * @param taskId The ID used to search for the task.
     * @return A Mono that emits the found Task.
     * @throws TaskNotFoundException if no task matches the ID.
     */
    @Override
    public Mono<Task> findTaskById(String taskId) {
        return Mono.fromCallable(() -> {
            // found: task returned from the map, or null if it does not exist.
            Task found = taskStore.get(taskId);
            if (found == null) {
                throw new TaskNotFoundException(taskId);
            }
            return found;
        });
    }

    /**
     * 1- Precondition: The taskStore map is available.
     * 2- Postcondition: All tasks are emitted one by one.
     * 3- Exceptions: Runtime errors may be emitted if reading the map fails.
     * 4- @param None.
     * 5- @throws RuntimeException if reading tasks fails unexpectedly.
     * 6- @return A Flux that emits each task.
     *
     * @return A Flux that emits each task.
     */
    @Override
    public Flux<Task> findAllTasks() {
        // taskStore.values(): collection of all saved Task objects.
        return Flux.fromIterable(taskStore.values());
    }

    /**
     * 1- Precondition: The taskStore map is available.
     * 2- Postcondition: All tasks are collected into a list.
     * 3- Exceptions: Runtime errors may be emitted if reading the map fails.
     * 4- @param None.
     * 5- @throws RuntimeException if reading tasks fails unexpectedly.
     * 6- @return A Mono that emits a list of all tasks.
     *
     * @return A Mono that emits a list of all tasks.
     */
    @Override
    public Mono<List<Task>> findAllTasksAsList() {
        // collectList(): converts the Flux stream into one List object.
        return findAllTasks().collectList();
    }
}
