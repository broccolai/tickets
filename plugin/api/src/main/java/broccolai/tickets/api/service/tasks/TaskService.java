package broccolai.tickets.api.service.tasks;

import broccolai.tickets.api.model.task.Task;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TaskService {

    /**
     * Run async
     */
    void async(@NonNull Runnable runnable);

    /**
     * Add repeating task
     */
    void schedule(@NonNull Task task);

    /**
     * Clear the current tasks from the manager
     */
    void clear();

}
