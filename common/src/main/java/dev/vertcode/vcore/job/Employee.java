package dev.vertcode.vcore.job;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An {@link Employee} controls all the tasks (Runnable) that are assigned to it.
 */
public class Employee {

    private final UUID identifier;
    private final ScheduledExecutorService executorService;

    public Employee(String jobName, int threads) {
        this.identifier = UUID.randomUUID();
        this.executorService = Executors.newScheduledThreadPool(threads, r -> {
            Thread thread = new Thread(r);

            // Set up the thread
            thread.setName("VCore Employee | " + jobName);
            return thread;
        });
    }

    /**
     * Adds a task to the {@link Employee}.
     *
     * @param runnable The task you want to run
     */
    public void addAssignment(Runnable runnable) {
        this.executorService.submit(runnable);
    }

    /**
     * Adds an {@link Assignment} with the given delay to the {@link Employee}.
     *
     * @param runnable The task you want to run
     * @param delay    The delay you want
     * @param timeUnit The time unit you want the delay to be in
     * @return The {@link Assignment} you just created
     */
    public Assignment addAssignment(Runnable runnable, Long delay, TimeUnit timeUnit) {
        return addAssignment(runnable, delay, null, timeUnit);
    }

    /**
     * Adds an {@link Assignment} with the given delay and interval to the {@link Employee}.
     *
     * @param runnable The task you want to run
     * @param delay    The delay you want
     * @param interval The interval you want
     * @param timeUnit The time unit you want the delay and interval to be in
     * @return The {@link Assignment} you just created
     */
    public Assignment addAssignment(Runnable runnable, Long delay, Long interval, TimeUnit timeUnit) {
        return new Assignment(runnable, delay, interval, timeUnit, this.executorService);
    }

    /**
     * Get the identifier of the {@link Employee}.
     *
     * @return The identifier
     */
    public UUID getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "identifier=" + this.identifier +
                '}';
    }
}
