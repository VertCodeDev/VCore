package dev.vertcode.vcore.collection;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExpiringSet<V> extends HashSet<V> {

    private static final ScheduledThreadPoolExecutor CLEANER_EXECUTOR = new ScheduledThreadPoolExecutor(1);

    private final Map<V, Instant> expireAt = new HashMap<>();
    private final Map<V, ScheduledFuture<?>> scheduledRemovals = new HashMap<>();

    private final long expireTime;
    private final TimeUnit expireTimeUnit;

    public ExpiringSet() {
        this(5, TimeUnit.SECONDS);
    }

    public ExpiringSet(long expireTime, TimeUnit expireTimeUnit) {
        this.expireTime = expireTime;
        this.expireTimeUnit = expireTimeUnit;
    }

    /**
     * Adds an entry to the map.
     *
     * @param value The value of the entry
     */
    public void addEntry(V value) {
        ScheduledFuture<?> scheduledRemoval = this.scheduledRemovals.remove(value);
        if (scheduledRemoval != null) {
            // Cancel the scheduled removal
            scheduledRemoval.cancel(false);
        }

        Instant expireAt = Instant.now().plusMillis(this.expireTimeUnit.toMillis(this.expireTime));

        this.expireAt.put(value, expireAt);
        this.scheduledRemovals.put(value, CLEANER_EXECUTOR.schedule(() -> {
            remove(value);
        }, this.expireTime, this.expireTimeUnit));
    }

    /**
     * Get the expiry time of the given value.
     *
     * @param value The value to get the expiry time of
     * @return The expiry time of the given value
     */
    public @Nullable Instant getExpireAt(V value) {
        return this.expireAt.get(value);
    }

    /**
     * Get the duration until the given value expires.
     *
     * @param value The value to get the duration of
     * @return The duration until the given value expires
     */
    public @Nullable Long getDuration(V value) {
        Instant expireAt = getExpireAt(value);
        if (expireAt == null) {
            return null;
        }

        return expireAt.toEpochMilli() - Instant.now().toEpochMilli();
    }

    /**
     * Get the duration until the given value expires.
     *
     * @param value    The value to get the duration of
     * @param timeUnit The time unit to return the duration in
     * @return The duration until the given value expires
     */
    public @Nullable Long getDuration(V value, TimeUnit timeUnit) {
        Long duration = getDuration(value);
        if (duration == null) {
            return null;
        }

        return timeUnit.convert(duration, TimeUnit.MILLISECONDS);
    }

    /**
     * Cancel all scheduled removals.
     */
    private void cancelAllScheduledRemovals() {
        for (ScheduledFuture<?> scheduledRemoval : new ArrayList<>(this.scheduledRemovals.values())) {
            scheduledRemoval.cancel(false);
        }

        this.scheduledRemovals.clear();
    }

    @Override
    public boolean add(V v) {
        addEntry(v);
        return super.add(v);
    }

    @Override
    public boolean remove(Object o) {
        ScheduledFuture<?> scheduledFuture = this.scheduledRemovals.remove(o);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }

        return super.remove(o);
    }

    @Override
    public void clear() {
        // Cancel all scheduled removals
        cancelAllScheduledRemovals();
        // Clear the expiry map
        this.expireAt.clear();
        // Clear the actual map
        super.clear();
    }

}
