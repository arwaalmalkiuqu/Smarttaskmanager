package taskmanager.api;

/**
 * Simple custom exception used when the weather API request or response fails.
 */
public class WeatherAPIException extends RuntimeException {

    /**
     * 1- Precondition: A weather-related operation failed and has an error message.
     * 2- Postcondition: A runtime exception object is created with the message and cause.
     * 3- Exceptions: This constructor does not throw another exception by itself.
     * 4- @param message The readable error message.
     * 4- @param cause The original error that caused this exception.
     * 5- @throws None directly.
     * 6- @return No direct return value because constructors create objects.
     *
     * @param message The readable error message.
     * @param cause The original error that caused this exception.
     */
    public WeatherAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
