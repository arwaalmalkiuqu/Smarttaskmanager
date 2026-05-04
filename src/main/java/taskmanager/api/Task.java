package taskmanager.api;

import java.time.LocalDateTime;

/**
 * Represents one task in the Smart Task Manager application.
 */
public class Task {

    // Unique task ID, such as "task-001".
    private final String id;

    // Short task name shown in the table.
    private String title;

    // Optional extra details about the task.
    private String description;

    // Date and time when the task should be completed.
    private LocalDateTime dueDateTime;

    // True when weather can affect this task.
    private boolean weatherSensitive;

    /**
     * 1- Precondition: id, title, and dueDateTime should describe a valid task.
     * 2- Postcondition: A Task object is created with the provided values.
     * 3- Exceptions: This constructor does not validate values or throw exceptions directly.
     * 4- @param id The unique task ID.
     * 4- @param title The task title.
     * 4- @param dueDateTime The due date and time.
     * 4- @param weatherSensitive True if weather can affect the task.
     * 5- @throws None directly.
     * 6- @return No direct return value because constructors create objects.
     *
     * @param id The unique task ID.
     * @param title The task title.
     * @param dueDateTime The due date and time.
     * @param weatherSensitive True if weather can affect the task.
     */
    public Task(String id, String title, LocalDateTime dueDateTime, boolean weatherSensitive) {
        this.id = id;
        this.title = title;
        this.dueDateTime = dueDateTime;
        this.weatherSensitive = weatherSensitive;
    }

    /**
     * 1- Precondition: The Task object exists.
     * 2- Postcondition: The task ID is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The unique task ID.
     *
     * @return The unique task ID.
     */
    public String getId() { return id; }

    /**
     * 1- Precondition: The Task object exists.
     * 2- Postcondition: The task title is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The task title.
     *
     * @return The task title.
     */
    public String getTitle() { return title; }

    /**
     * 1- Precondition: The Task object exists.
     * 2- Postcondition: The task description is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The task description, or null if it was not set.
     *
     * @return The task description, or null if it was not set.
     */
    public String getDescription() { return description; }

    /**
     * 1- Precondition: The Task object exists.
     * 2- Postcondition: The due date and time are returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The due date and time.
     *
     * @return The due date and time.
     */
    public LocalDateTime getDueDateTime() { return dueDateTime; }

    /**
     * 1- Precondition: The Task object exists.
     * 2- Postcondition: The weather-sensitive value is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return True if weather can affect this task.
     *
     * @return True if weather can affect this task.
     */
    public boolean isWeatherSensitive() { return weatherSensitive; }

    /**
     * 1- Precondition: The caller provides a new title.
     * 2- Postcondition: The task title is updated.
     * 3- Exceptions: No exception is expected in this setter.
     * 4- @param title The new title.
     * 5- @throws None.
     * 6- @return No return value because this method is void.
     *
     * @param title The new title.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * 1- Precondition: The caller provides a new description.
     * 2- Postcondition: The task description is updated.
     * 3- Exceptions: No exception is expected in this setter.
     * 4- @param description The new description.
     * 5- @throws None.
     * 6- @return No return value because this method is void.
     *
     * @param description The new description.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * 1- Precondition: The caller provides a new due date and time.
     * 2- Postcondition: The task due date and time are updated.
     * 3- Exceptions: No exception is expected in this setter.
     * 4- @param dueDateTime The new due date and time.
     * 5- @throws None.
     * 6- @return No return value because this method is void.
     *
     * @param dueDateTime The new due date and time.
     */
    public void setDueDateTime(LocalDateTime dueDateTime) { this.dueDateTime = dueDateTime; }

    /**
     * 1- Precondition: The caller provides a true or false weather-sensitive value.
     * 2- Postcondition: The task weather-sensitive value is updated.
     * 3- Exceptions: No exception is expected in this setter.
     * 4- @param weatherSensitive True if weather can affect the task.
     * 5- @throws None.
     * 6- @return No return value because this method is void.
     *
     * @param weatherSensitive True if weather can affect the task.
     */
    public void setWeatherSensitive(boolean weatherSensitive) { this.weatherSensitive = weatherSensitive; }

    /**
     * 1- Precondition: The Task object exists.
     * 2- Postcondition: A readable text version of the task is returned.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return A string containing the main task values.
     *
     * @return A string containing the main task values.
     */
    @Override
    public String toString() {
        return "Task{id='" + id + "', title='" + title + "', due=" + dueDateTime + ", weatherSensitive=" + weatherSensitive + "}";
    }
}
