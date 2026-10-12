package states;

import entities.Task;
import enums.TaskStatus;

public interface TaskState {
    void startProgress(Task task);
    void completeTask(Task task);
    void blockTask(Task task);
    void reopenTask(Task task);
    TaskStatus getStatus();
}
