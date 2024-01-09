package dev.vertcode.vcore.job;

import java.util.concurrent.TimeUnit;

/**
 * All the asynchronous jobs that are available.
 */
public enum Jobs {

    //General
    DEFAULT_JOB("Default Job", 4),
    FRONTEND_JOB("Frontend Job", 4),
    BACKEND_JOB("Backend Job", 4),

    //MC
    MC_PLAYER_JOB("Minecraft Player Job", 8),
    MC_PACKET_JOB("Minecraft Packet Job", 12),
    MC_EVENT_JOB("Minecraft Event Job", 24);

    private final Employee employee;

    Jobs(String jobName, int threads) {
        this.employee = new Employee(jobName, threads);
    }

    /**
     * Add a task to the {@link Employee}.
     *
     * @param runnable The task you want to run
     */
    public void addAssignment(Runnable runnable) {
        this.employee.addAssignment(runnable);
    }

    /**
     * Add a task to the {@link Employee} with the given delay.
     *
     * @param runnable The task you want to run
     * @param delay The delay you want
     * @param timeUnit The time unit you want the delay to be in
     * @return The {@link Assignment} you just created
     */
    public Assignment addAssignment(Runnable runnable, Long delay, TimeUnit timeUnit) {
        return this.employee.addAssignment(runnable, delay, timeUnit);
    }

    /**
     * Add a task to the {@link Employee} with the given delay and interval.
     *
     * @param runnable The task you want to run
     * @param delay The delay you want
     * @param interval The interval you want
     * @param timeUnit The time unit you want the delay and interval to be in
     * @return The {@link Assignment} you just created
     */
    public Assignment addAssignment(Runnable runnable, Long delay, Long interval, TimeUnit timeUnit) {
        return this.employee.addAssignment(runnable, delay, interval, timeUnit);
    }

    /**
     * Get the {@link Employee} of this job.
     *
     * @return The {@link Employee} of this job
     */
    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "Jobs{" +
                "employee=" + employee +
                '}';
    }
}
