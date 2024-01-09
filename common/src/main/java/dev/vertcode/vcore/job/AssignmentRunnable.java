package dev.vertcode.vcore.job;

/**
 * This class is used to run a task asynchronously.
 */
public abstract class AssignmentRunnable implements Runnable {

    private Assignment assignment;

    /**
     * Initialize the {@link AssignmentRunnable}.
     *
     * @param assignment The assignment you want to initialize
     */
    public void initialize(Assignment assignment) {
        this.assignment = assignment;
    }

    /**
     * Check if the {@link Assignment} is cancelled.
     *
     * @return if the {@link Assignment} is cancelled
     */
    public boolean isCancelled() {
        return this.assignment.isCancelled();
    }

    /**
     * Cancel the {@link Assignment}.
     *
     * @param interrupt if the {@link Assignment} should be interrupted
     * @return if the {@link Assignment} could be cancelled
     */
    public boolean cancel(boolean interrupt) {
        return this.assignment.cancel(interrupt);
    }

    /**
     * Cancel the {@link Assignment}.
     *
     * @return if the {@link Assignment} could be cancelled
     */
    public boolean cancel() {
        return this.assignment.cancel(false);
    }

    /**
     * Get the {@link Assignment}.
     *
     * @return the {@link Assignment}
     */
    public Assignment getAssignment() {
        return this.assignment;
    }
}
