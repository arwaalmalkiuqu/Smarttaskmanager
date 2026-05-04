package taskmanager.api;

/**
 * Simple custom exception used when a task ID cannot be found.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * 1- Precondition: A task search or delete operation did not find the requested ID.
     * 2- Postcondition: A runtime exception object is created with the missing task ID.
     * 3- Exceptions: This constructor does not throw another exception by itself.
     * 4- @param taskId The ID of the task that was not found.
     * 5- @throws None directly.
     * 6- @return No direct return value because constructors create objects.
     *
     * @param taskId The ID of the task that was not found.
     */
    public TaskNotFoundException(String taskId) {
        super("Task not found: " + taskId);
    }
}
