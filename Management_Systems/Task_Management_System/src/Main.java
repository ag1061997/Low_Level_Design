import core.TaskManagementSystem;
import entities.*;
import enums.TaskPriority;
import enums.TaskStatus;
import strategy.SortByDueDate;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManagementSystem taskManagementSystem = new TaskManagementSystem();

        User user1 = taskManagementSystem.createUser("John Doe", "john@example.com");
        User user2 = taskManagementSystem.createUser("Jane Smith", "jane@example.com");

        TaskList taskList1 = taskManagementSystem.createTaskList("Enhancements");
        TaskList taskList2 = taskManagementSystem.createTaskList("Bug Fix");

        Task task1 = taskManagementSystem.createTask("Enhancement Task", "Launch New Feature",
                LocalDate.now().plusDays(2), TaskPriority.LOW, user1.getId());
        Task subtask1 = taskManagementSystem.createTask( "Enhancement sub task", "Design UI/UX",
                LocalDate.now().plusDays(1), TaskPriority.MEDIUM, user1.getId());
        Task task2 = taskManagementSystem.createTask("Bug Fix Task", "Fix API Bug",
                LocalDate.now().plusDays(3), TaskPriority.HIGH, user2.getId());

        task1.addSubtask(subtask1);
        taskList1.addTask(task1);
        taskList2.addTask(task2);
        taskList1.display();

        subtask1.startProgress();
        subtask1.setAssignee(user2);
        taskList1.display();

        List<Task> searchResults = taskManagementSystem.searchTasks("Task", new SortByDueDate());
        System.out.println("\nTasks with keyword Task:");
        for (Task task : searchResults) {
            System.out.println(task.getTitle());
        }

        List<Task> filteredTasks = taskManagementSystem.listTasksByStatus(TaskStatus.TODO);
        System.out.println("\nTODO Tasks:");
        for (Task task : filteredTasks) {
            System.out.println(task.getTitle());
        }
        subtask1.completeTask();

        List<Task> userTaskList = taskManagementSystem.listTasksByUser(user2.getId());
        System.out.println("\nTask for " + user2.getName() + ":");
        for (Task task : userTaskList) {
            System.out.println(task.getTitle());
        }
        taskList1.display();

        taskManagementSystem.deleteTask(task2.getId());

        task1.addTag(new Tag("feature"));
        task1.addTag(new Tag("feature")); // duplicate, ignored by value equality
        subtask1.addComment(new Comment("Initial mockups ready", user1));

        System.out.println("\nTags on " + task1.getTitle() + ":");
        for (Tag tag : task1.getTags()) {
            System.out.println("  " + tag.getName());
        }

        System.out.println("\nComments on " + subtask1.getTitle() + ":");
        for (Comment comment : subtask1.getComments()) {
            System.out.println("  " + comment.getAuthor().getName() + ": " + comment.getContent());
        }

        System.out.println("\nActivity log for " + subtask1.getTitle() + ":");
        subtask1.printActivityLog();
    }
}