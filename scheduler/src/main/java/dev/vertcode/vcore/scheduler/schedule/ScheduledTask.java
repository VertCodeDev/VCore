package dev.vertcode.vcore.scheduler.schedule;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTask {

    private final Future<?> future;

    public ScheduledTask(Runnable runnable, Long delay, TimeUnit timeUnit, ScheduledExecutorService executorService) {
        if (runnable instanceof ScheduleRunnable scheduleRunnable) {
            scheduleRunnable.initializeTask(this);
        }

        this.future = executorService.schedule(runnable, delay, timeUnit);
    }

    public ScheduledTask(Runnable runnable, Long delay, Long interval, TimeUnit timeUnit, ScheduledExecutorService executorService) {
        if (runnable instanceof ScheduleRunnable scheduleRunnable) {
            scheduleRunnable.initializeTask(this);
        }

        this.future = executorService.scheduleAtFixedRate(runnable, delay, interval, timeUnit);
    }

    /**
     * Cancel a {@link ScheduledTask}.
     *
     * @return if the {@link ScheduledTask} has been canceled
     */
    public boolean cancel() {
        return this.future.cancel(false);
    }

    /**
     * Cancel a {@link ScheduledTask}.
     *
     * @param mayInterruptIfRunning if the task may be interrupted if running
     * @return if the {@link ScheduledTask} has been canceled
     */
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.future.cancel(mayInterruptIfRunning);
    }

    /**
     * Check if the {@link ScheduledTask} has been canceled.
     *
     * @return if the {@link ScheduledTask} has been canceled
     */
    public boolean isCancelled() {
        return this.future.isCancelled();
    }

    /**
     * Check if the {@link ScheduledTask} is done.
     *
     * @return if the {@link ScheduledTask} is done
     */
    public boolean isDone() {
        return this.future.isDone();
    }

    /**
     * Get the {@link Future} of the {@link ScheduledTask}.
     *
     * @return the {@link Future} of the {@link ScheduledTask}
     */
    public Future<?> getFuture() {
        return this.future;
    }

}
