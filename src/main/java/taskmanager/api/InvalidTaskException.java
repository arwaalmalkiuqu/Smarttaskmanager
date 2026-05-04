package taskmanager.api;

/**
 * Simple custom exception used when task data is not valid.
 */
public class InvalidTaskException extends RuntimeException {

    /**
     * 1- Precondition: The caller has detected invalid task data.
     * 2- Postcondition: A runtime exception object is created with a clear message.
     * 3- Exceptions: This constructor does not throw another exception by itself.
     * 4- @param message The error message that explains why the task is invalid.
     * 5- @throws None directly.
     * 6- @return No direct return value because constructors create objects.
     *
     * @param message The error message that explains why the task is invalid.
     */
    public InvalidTaskException(String message) {
        super(message);
    }
}
