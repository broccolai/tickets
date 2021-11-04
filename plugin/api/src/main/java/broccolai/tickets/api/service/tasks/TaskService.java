package broccolai.tickets.api.service.tasks;

import broccolai.tickets.api.model.task.RepeatTask;
import broccolai.tickets.api.model.task.Task;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TaskService {

    void sync(@NonNull Runnable runnable);

    void async(@NonNull Runnable runnable);

    void schedule(@NonNull Task task);

    void schedule(@NonNull RepeatTask task);

    void clear();

}
