package broccolai.tickets.core.tasks;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface TaskManager {

    /**
     * Run something async
     *
     * @param runnable Runnable
     */
    void async(@NonNull Runnable runnable);

    /**
     * Add a new repeating task to the manager
     *
     * @param runnable Runnable
     * @param delay    Delay long
     * @param repeat   Repeat long
     */
    void addRepeatingTask(@NonNull Runnable runnable, long delay, long repeat);

    /**
     * Clear the current tasks from the manager
     */
    void clear();

}
