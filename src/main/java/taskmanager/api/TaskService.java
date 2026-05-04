package taskmanager.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * Defines reactive task operations for adding, removing, finding, and listing tasks.
 */
public interface TaskService {

    /**
     * 1- Precondition: The task is not null and contains valid data.
     * 2- Postcondition: The task is saved in the task storage.
     * 3- Exceptions: InvalidTaskException may be emitted if the task is invalid.
     * 4- @param task The task to add.
     * 5- @throws InvalidTaskException if the task data is invalid.
     * 6- @return A Mono that completes when the task is added.
     *
     * @param task The task to add.
     * @return A Mono that completes when the task is added.
     * @throws InvalidTaskException if the task data is invalid.
     */
    Mono<Void> addTask(Task task);

    /**
     * 1- Precondition: The taskId belongs to an existing task.
     * 2- Postcondition: The matching task is removed from storage.
     * 3- Exceptions: TaskNotFoundException may be emitted if the task does not exist.
     * 4- @param taskId The ID of the task to remove.
     * 5- @throws TaskNotFoundException if no task matches the ID.
     * 6- @return A Mono that completes when the task is removed.
     *
     * @param taskId The ID of the task to remove.
     * @return A Mono that completes when the task is removed.
     * @throws TaskNotFoundException if no task matches the ID.
     */
    Mono<Void> removeTask(String taskId);

    /**
     * 1- Precondition: The taskId is provided.
     * 2- Postcondition: The matching task is returned if it exists.
     * 3- Exceptions: TaskNotFoundException may be emitted if the task does not exist.
     * 4- @param taskId The ID used for searching.
     * 5- @throws TaskNotFoundException if no task matches the ID.
     * 6- @return A Mono that emits the found task.
     *
     * @param taskId The ID used for searching.
     * @return A Mono that emits the found task.
     * @throws TaskNotFoundException if no task matches the ID.
     */
    Mono<Task> findTaskById(String taskId);

    /**
     * 1- Precondition: The task storage is available.
     * 2- Postcondition: All tasks are emitted one by one.
     * 3- Exceptions: Runtime errors may be emitted if reading tasks fails.
     * 4- @param None.
     * 5- @throws RuntimeException if reading tasks fails unexpectedly.
     * 6- @return A Flux that emits every task.
     *
     * @return A Flux that emits every task.
     */
    Flux<Task> findAllTasks();

    /**
     * 1- Precondition: The task storage is available.
     * 2- Postcondition: All tasks are collected into one list.
     * 3- Exceptions: Runtime errors may be emitted if reading tasks fails.
     * 4- @param None.
     * 5- @throws RuntimeException if reading tasks fails unexpectedly.
     * 6- @return A Mono that emits a list of all tasks.
     *
     * @return A Mono that emits a list of all tasks.
     */
    Mono<List<Task>> findAllTasksAsList();
}
