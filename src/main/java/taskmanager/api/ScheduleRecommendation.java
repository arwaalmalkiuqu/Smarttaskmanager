package taskmanager.api;

/**
 * Stores one schedule suggestion for one task.
 *
 * <p>Variable comments:</p>
 * <ul>
 *   <li>task: the task being checked.</li>
 *   <li>recommendation: the text suggestion shown to the user.</li>
 * </ul>
 *
 * 1- Precondition: The task and recommendation text should describe the same schedule decision.
 * 2- Postcondition: An immutable recommendation object is created.
 * 3- Exceptions: The record itself does not validate values or throw exceptions.
 * 4- @param task The task this recommendation belongs to.
 * 4- @param recommendation The readable recommendation message.
 * 5- @throws None directly.
 * 6- @return No direct return value because records are created through their constructor.
 *
 * @param task The task this recommendation belongs to.
 * @param recommendation The readable recommendation message.
 */
public record ScheduleRecommendation(Task task, String recommendation) {
}
