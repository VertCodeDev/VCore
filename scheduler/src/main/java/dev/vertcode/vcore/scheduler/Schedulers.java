package dev.vertcode.vcore.scheduler;

import dev.vertcode.vcore.scheduler.schedule.ScheduledTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public enum Schedulers {

    //General
    DEFAULT("Default Schedule", 4),
    FRONTEND("Frontend Schedule", 4),
    BACKEND("Backend Schedule", 4),

    //MC
    MC_PLAYER("Minecraft Player Schedule", 8),
    MC_PACKET("Minecraft Packet Schedule", 12),
    MC_EVENT("Minecraft Event Schedule", 24)
    ;

    private final ScheduledExecutorService executorService;

    Schedulers(String scheduleName, int threads) {
        this.executorService = Executors.newScheduledThreadPool(threads, r -> {
            Thread thread = new Thread(r);

            // Set up the thread
            thread.setName("VCore Scheduler | " + scheduleName);

            return thread;
        });
    }

    /**
     * Runs a task using the executor service.
     *
     * @param runnable The task you want to run
     */
    public void run(Runnable runnable) {
        this.executorService.submit(runnable);
    }

    /**
     * Creates a {@link ScheduledTask} for this executor service with the given initial delay.
     *
     * @param runnable The task you want to run
     * @param delay The initial delay you want
     * @param timeUnit The {@link TimeUnit} you want the delay to be in
     * @return The {@link ScheduledTask} you just created
     */
    public ScheduledTask run(Runnable runnable, Long delay, TimeUnit timeUnit) {
        return new ScheduledTask(runnable, delay, timeUnit, this.executorService);
    }

    /**
     * Creates a {@link ScheduledTask} for this executor service with the given initial delay and the given interval,
     * this means that it runs every x times per x {@link TimeUnit}.
     *
     * @param runnable The task you want to run
     * @param delay The initial delay you want
     * @param interval The run interval you want
     * @param timeUnit The {@link TimeUnit} you want the delay & interval to be in
     * @return The {@link ScheduledTask} you just created
     */
    public ScheduledTask run(Runnable runnable, Long delay, Long interval, TimeUnit timeUnit) {
        return new ScheduledTask(runnable, delay, interval, timeUnit, this.executorService);
    }

}
