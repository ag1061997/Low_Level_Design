package entities;

import enums.TaskPriority;

import java.time.LocalDate;

public class Task {
    private final String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskPriority priority;
    private final User createdBy;
    private User assignee;
    private Task
}
