package dev.vertcode.vcore.job;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An {@link Assignment} is a task that is executed by a {@link Employee}.
 */
public class Assignment {

    private final UUID identifier;
    private ScheduledFuture<?> scheduledFuture;
    private Future<?> future;

    public Assignment(Runnable runnable, Long delay, Long interval, TimeUnit timeUnit, ScheduledExecutorService executorService) {
        this.identifier = UUID.randomUUID();

        if (runnable instanceof AssignmentRunnable assignmentRunnable) {
            assignmentRunnable.initialize(this);
        }

        if (delay != null && interval == null) {
            this.future = executorService.schedule(runnable, delay, timeUnit);
            return;
        }

        if (interval == null || delay == null) {
            throw new NullPointerException("Interval & Delay cannot be null at the same time!");
        }

        this.scheduledFuture = executorService.scheduleAtFixedRate(runnable, delay, interval, timeUnit);
    }

    /**
     * Cancel the {@link Assignment}.
     *
     * @return if the {@link Assignment} could be cancelled
     */
    public boolean cancel() {
        return this.cancel(false);
    }

    /**
     * Cancel the {@link Assignment}.
     *
     * @param interrupt if the {@link Assignment} should be interrupted
     * @return if the {@link Assignment} could be cancelled
     */
    public boolean cancel(boolean interrupt) {
        if (this.future != null) {
            return this.future.cancel(interrupt);
        }

        return this.scheduledFuture.cancel(interrupt);
    }

    /**
     * Check if the {@link Assignment} is cancelled.
     *
     * @return if the {@link Assignment} is cancelled
     */
    public boolean isCancelled() {
        if (this.future == null && this.scheduledFuture == null) {
            return true;
        }

        return Objects.requireNonNullElseGet(this.future, () -> this.scheduledFuture).isCancelled();

    }

    /**
     * Check if the {@link Assignment} is done.
     *
     * @return if the {@link Assignment} is done
     */
    public boolean isDone() {
        return this.future != null && this.future.isDone();
    }

    /**
     * Get the {@link UUID} of this {@link Assignment}.
     *
     * @return the {@link UUID} of this {@link Assignment}
     */
    public UUID getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return "Assignment{" + "identifier=" + identifier + '}';
    }
}
