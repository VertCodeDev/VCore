package dev.vertcode.vcore.scheduler.schedule;

public abstract class ScheduleRunnable implements Runnable {

    private ScheduledTask scheduledTask;

    /**
     * Initialize the {@link ScheduleRunnable}.
     *
     * @param scheduledTask the task it belongs to
     */
    public void initializeTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    /**
     * Check if the {@link ScheduleRunnable} is canceled.
     *
     * @return if the {@link ScheduleRunnable} is canceled
     */
    public boolean isCancelled() {
        return this.scheduledTask.isCancelled();
    }

    /**
     * Cancel the {@link ScheduleRunnable}.
     *
     * @return if the {@link ScheduleRunnable} has been canceled
     */
    public boolean cancel() {
        return cancel(true);
    }

    /**
     * Cancel the {@link ScheduleRunnable}.
     *
     * @param interrupt should it interrupt the process if it's going on
     * @return if the {@link ScheduleRunnable} has been canceled
     */
    public boolean cancel(boolean interrupt) {
        return this.scheduledTask.cancel(interrupt);
    }

}
