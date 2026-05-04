package taskmanager.ui;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import taskmanager.api.SchedulePlanner;
import taskmanager.api.ScheduleRecommendation;
import taskmanager.api.Task;
import taskmanager.api.TaskManager;
import taskmanager.api.TaskService;
import taskmanager.impl.DefaultTaskManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Main Swing window for the Smart Task Manager.
 * Handles all UI interactions and delegates to the API layer.
 */
public class SmartTaskManagerFrame extends JFrame {

    // Dark mode colors
    private static final Color BG       = new Color(45, 45, 45);
    private static final Color BG_TABLE = new Color(35, 35, 35);
    private static final Color FG       = new Color(210, 210, 210);
    private static final Color BTN      = new Color(70, 70, 70);
    private static final Color HEADER   = new Color(100, 149, 200);

    private final TaskManager taskManager;
    private final TaskService taskService;
    private final SchedulePlanner schedulePlanner;

    private final JTable taskTable;
    private final DefaultTableModel tableModel;
    private static final String[] COLUMNS =
            {"ID", "Title", "Due Time", "Weather Sensitive", "Status"};

    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JButton checkWeatherButton;
    private final JButton suggestButton;

    private final JLabel statusLabel;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Builds the window and loads tasks on startup.
     *
     * @param taskManager The configured TaskManager from MainApp.
     */
    public SmartTaskManagerFrame(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.taskService = ((DefaultTaskManager) taskManager).getTaskService();
        this.schedulePlanner = taskManager.getPlanner();

        setTitle("Smart Task Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);

        // Table setup - read only
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        taskTable = new JTable(tableModel);
        taskTable.setBackground(BG_TABLE);
        taskTable.setForeground(FG);
        taskTable.setGridColor(new Color(60, 60, 60));
        taskTable.setRowHeight(25);
        taskTable.setSelectionBackground(new Color(70, 100, 130));
        taskTable.setSelectionForeground(Color.WHITE);
        taskTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        taskTable.getTableHeader().setBackground(BG);
        taskTable.getTableHeader().setForeground(HEADER);
        taskTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.getViewport().setBackground(BG_TABLE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Buttons - edit/delete/weather need a row selected
        addButton          = makeButton("Add Task");
        editButton         = makeButton("Edit");
        deleteButton       = makeButton("Delete");
        checkWeatherButton = makeButton("Check Weather");
        suggestButton      = makeButton("Suggest Schedule");

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        checkWeatherButton.setEnabled(false);

        // Status bar at the bottom
        statusLabel = new JLabel("Ready.");
        statusLabel.setForeground(new Color(150, 150, 150));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setBackground(new Color(38, 38, 38));
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        buttonPanel.setBackground(BG);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkWeatherButton);
        buttonPanel.add(suggestButton);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane,  BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        wireEvents();
        loadTasks();
    }

    /**
     * Creates a styled dark mode button.
     *
     * @param text Button label.
     * @return Styled JButton.
     */
    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BTN);
        btn.setForeground(FG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return btn;
    }

    /**
     * Connects buttons and table selection to their actions.
     */
    private void wireEvents() {
        // Enable row-dependent buttons only when a row is selected
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = taskTable.getSelectedRow() >= 0;
            editButton.setEnabled(selected);
            deleteButton.setEnabled(selected);
            checkWeatherButton.setEnabled(selected);
        });

        addButton.addActionListener(e -> showAddTaskDialog());
        editButton.addActionListener(e -> showEditTaskDialog());
        deleteButton.addActionListener(e -> deleteSelectedTask());
        checkWeatherButton.addActionListener(e -> updateWeatherForSelectedTask());
        suggestButton.addActionListener(e -> suggestSchedule());
    }

    /**
     * Loads tasks from the manager and refreshes the table.
     * Runs on a background thread, updates UI on the EDT.
     */
    private void loadTasks() {
        Mono.just(taskManager.getTasks())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(tasks -> SwingUtilities.invokeLater(() -> populateTable(tasks)))
                .subscribe(tasks -> {},
                        err -> SwingUtilities.invokeLater(() ->
                                statusLabel.setText("Error: " + err.getMessage())));
    }

    /**
     * Clears the table and fills it with the given tasks.
     *
     * @param tasks List of tasks to display.
     */
    private void populateTable(List<Task> tasks) {
        tableModel.setRowCount(0);
        for (Task t : tasks) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getTitle(),
                    t.getDueDateTime().format(DATE_FMT),
                    t.isWeatherSensitive() ? "Yes" : "No",
                    "-"
            });
        }
        statusLabel.setText("Loaded " + tasks.size() + " task(s).");
    }

    /**
     * Shows a dialog to add a new task.
     * Validates input before saving.
     */
    private void showAddTaskDialog() {
        JTextField titleField = new JTextField(20);
        JTextField descField  = new JTextField(20);
        JTextField dateField  = new JTextField("2026-12-31 09:00", 20);
        JCheckBox  wxCheck    = new JCheckBox("Weather Sensitive?");

        JPanel panel = buildFormPanel(
                new String[]{"Title:", "Description:", "Due (yyyy-MM-dd HH:mm):", ""},
                new JComponent[]{titleField, descField, dateField, wxCheck}
        );

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        String title = titleField.getText().trim();
        if (title.isEmpty()) { showError("Title cannot be empty."); return; }

        LocalDateTime due;
        try {
            due = LocalDateTime.parse(dateField.getText().trim(), DATE_FMT);
        } catch (Exception ex) {
            showError("Invalid date format. Use: yyyy-MM-dd HH:mm");
            return;
        }

        Task newTask = new Task(
                "task-" + UUID.randomUUID().toString().substring(0, 8),
                title, due, wxCheck.isSelected()
        );
        newTask.setDescription(descField.getText().trim());

        taskService.addTask(newTask)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(v -> SwingUtilities.invokeLater(() -> {
                    loadTasks();
                    statusLabel.setText("Task '" + title + "' added.");
                }))
                .doOnError(err -> SwingUtilities.invokeLater(() ->
                        showError("Could not add: " + err.getMessage())))
                .subscribe();
    }

    /**
     * Loads the selected task and opens the edit dialog.
     */
    private void showEditTaskDialog() {
        int row = taskTable.getSelectedRow();
        if (row < 0) return;
        String taskId = (String) tableModel.getValueAt(row, 0);

        taskService.findTaskById(taskId)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(task -> SwingUtilities.invokeLater(() -> openEditDialog(task)))
                .doOnError(err -> SwingUtilities.invokeLater(() ->
                        showError("Could not find task: " + err.getMessage())))
                .subscribe();
    }

    /**
     * Opens an edit dialog pre-filled with the task's current values.
     *
     * @param task The task to edit.
     */
    private void openEditDialog(Task task) {
        JTextField titleField = new JTextField(task.getTitle(), 20);
        JTextField descField  = new JTextField(
                task.getDescription() != null ? task.getDescription() : "", 20);
        JTextField dateField  = new JTextField(
                task.getDueDateTime().format(DATE_FMT), 20);
        JCheckBox wxCheck = new JCheckBox("Weather Sensitive?",
                task.isWeatherSensitive());

        JPanel panel = buildFormPanel(
                new String[]{"Title:", "Description:", "Due (yyyy-MM-dd HH:mm):", ""},
                new JComponent[]{titleField, descField, dateField, wxCheck}
        );

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Edit Task", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        String newTitle = titleField.getText().trim();
        if (newTitle.isEmpty()) { showError("Title cannot be empty."); return; }

        LocalDateTime newDue;
        try {
            newDue = LocalDateTime.parse(dateField.getText().trim(), DATE_FMT);
        } catch (Exception ex) {
            showError("Invalid date format. Use: yyyy-MM-dd HH:mm");
            return;
        }

        task.setTitle(newTitle);
        task.setDescription(descField.getText().trim());
        task.setDueDateTime(newDue);
        task.setWeatherSensitive(wxCheck.isSelected());

        taskService.addTask(task)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(v -> SwingUtilities.invokeLater(() -> {
                    loadTasks();
                    statusLabel.setText("Task '" + newTitle + "' updated.");
                }))
                .doOnError(err -> SwingUtilities.invokeLater(() ->
                        showError("Could not update: " + err.getMessage())))
                .subscribe();
    }

    /**
     * Asks for confirmation then deletes the selected task.
     */
    private void deleteSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row < 0) return;

        String taskId    = (String) tableModel.getValueAt(row, 0);
        String taskTitle = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete \"" + taskTitle + "\"?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        taskService.removeTask(taskId)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(v -> SwingUtilities.invokeLater(() -> {
                    loadTasks();
                    statusLabel.setText("Task '" + taskTitle + "' deleted.");
                }))
                .doOnError(err -> SwingUtilities.invokeLater(() ->
                        showError("Could not delete: " + err.getMessage())))
                .subscribe();
    }

    /**
     * Fetches weather for Jeddah and marks the selected task as SAFE or RISKY.
     */
    private void updateWeatherForSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row < 0) return;

        String taskId = (String) tableModel.getValueAt(row, 0);
        statusLabel.setText("Fetching weather...");

        taskManager.fetchWeather("Jeddah")
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(forecast -> SwingUtilities.invokeLater(() -> {
                    // Rain > 60% = RISKY, otherwise SAFE
                    String status = forecast.getPrecipitationProbability() > 0.6
                            ? "RISKY" : "SAFE";
                    updateStatusInTable(taskId, status);
                    statusLabel.setText("Weather: " + forecast.getCondition() +
                            ", " + forecast.getTemperatureCelsius() + "C");
                }))
                .doOnError(err -> SwingUtilities.invokeLater(() ->
                        statusLabel.setText("Failed: " + err.getMessage())))
                .subscribe();
    }

    /**
     * Gets schedule recommendations for all tasks and shows them in a dialog.
     */
    private void suggestSchedule() {
        List<Task> tasks = taskManager.getTasks();
        if (tasks.isEmpty()) {
            showError("No tasks found.");
            return;
        }

        statusLabel.setText("Generating suggestions...");

        schedulePlanner.suggestScheduleForLocation(tasks, "Jeddah")
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(recs -> SwingUtilities.invokeLater(() -> {
                    StringBuilder sb = new StringBuilder("Recommendations:\n\n");
                    for (ScheduleRecommendation rec : recs) {
                        sb.append("- ").append(rec.task().getTitle())
                                .append(": ").append(rec.recommendation()).append("\n\n");
                        // Update table status based on recommendation text
                        String status = rec.recommendation().contains("RISKY")
                                ? "RISKY" : "SAFE";
                        updateStatusInTable(rec.task().getId(), status);
                    }
                    JOptionPane.showMessageDialog(this, sb.toString(),
                            "Schedule Suggestions", JOptionPane.INFORMATION_MESSAGE);
                    statusLabel.setText("Done.");
                }))
                .doOnError(err -> SwingUtilities.invokeLater(() ->
                        showError("Failed: " + err.getMessage())))
                .subscribe();
    }

    /**
     * Updates the Status column for the matching task row.
     *
     * @param taskId The task ID to find.
     * @param status The new status text.
     */
    private void updateStatusInTable(String taskId, String status) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (taskId.equals(tableModel.getValueAt(i, 0))) {
                tableModel.setValueAt(status, i, 4);
                break;
            }
        }
    }

    /**
     * Shows an error dialog with the given message.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Builds a form panel with labels on the left and inputs on the right.
     *
     * @param labels     Label texts.
     * @param components Input components.
     * @return A JPanel with the form layout.
     */
    private JPanel buildFormPanel(String[] labels, JComponent[] components) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(FG);
            panel.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(components[i], gbc);
        }
        return panel;
    }
}