package taskmanager.api;

import taskmanager.ui.SmartTaskManagerFrame;
import java.time.LocalDateTime;

/**
 * Starts the Smart Task Manager application and loads sample tasks.
 */
public class MainApp {

    /**
     * 1- Precondition: Java is running and the project dependencies are available.
     * 2- Postcondition: The task manager is created, sample tasks are added, and the UI is shown.
     * 3- Exceptions: Runtime errors may occur if the UI or task manager cannot be created.
     * 4- @param args Command-line arguments; they are not used in this application.
     * 5- @throws RuntimeException if startup fails unexpectedly.
     * 6- @return No return value because this is a void main method.
     *
     * @param args Command-line arguments; they are not used in this application.
     */
    public static void main(String[] args) {

        // Main manager object used to control tasks and weather services.
        TaskManager tm = TaskManager.builder()
                .withWeatherApiKey("638b2ec2227f0c12ebddc9b2da0f4c43")
                .build();

        // First sample task shown when the program starts.
        Task task1 = new Task(
                "task-001",
                "Morning Run",
                LocalDateTime.now().plusHours(2),
                true
        );
        task1.setDescription("Jog around the park");

        // Second sample task shown when the program starts.
        Task task2 = new Task(
                "task-002",
                "Coding Session",
                LocalDateTime.now().plusHours(4),
                false
        );
        task2.setDescription("Work on the Smart Task Manager project");

        // Add the sample tasks into the manager.
        tm.addTask(task1);
        tm.addTask(task2);

        // Simple console message used to confirm that tasks were loaded.
        System.out.println("Tasks loaded: " + tm.getTasks().size());

        // Main Swing window for the application.
        SmartTaskManagerFrame frame = new SmartTaskManagerFrame(tm);
        javax.swing.SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
