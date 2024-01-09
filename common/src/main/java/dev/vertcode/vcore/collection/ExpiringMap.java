package dev.vertcode.vcore.collection;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A map that automatically removes entries after a given time.
 *
 * @param <K> The key type of the map
 * @param <V> The value type of the map
 */
public class ExpiringMap<K, V> extends HashMap<K, V> {

    private static final ScheduledThreadPoolExecutor CLEANER_EXECUTOR = new ScheduledThreadPoolExecutor(1);

    private final Map<K, Instant> expireAt = new HashMap<>();
    private final Map<K, ScheduledFuture<?>> scheduledRemovals = new HashMap<>();

    private final long expireTime;
    private final TimeUnit expireTimeUnit;

    public ExpiringMap() {
        this(5, TimeUnit.SECONDS);
    }

    public ExpiringMap(long expireTime, TimeUnit expireTimeUnit) {
        this.expireTime = expireTime;
        this.expireTimeUnit = expireTimeUnit;
    }

    /**
     * Adds an entry to the map.
     *
     * @param key The key of the entry
     */
    public void addEntry(K key) {
        ScheduledFuture<?> scheduledRemoval = this.scheduledRemovals.remove(key);
        if (scheduledRemoval != null) {
            scheduledRemoval.cancel(false);
        }

        Instant expireAt = Instant.now().plusMillis(this.expireTimeUnit.toMillis(this.expireTime));

        this.expireAt.put(key, expireAt);
        this.scheduledRemovals.put(key, CLEANER_EXECUTOR.schedule(() -> {
            remove(key);
        }, this.expireTime, this.expireTimeUnit));
    }

    /**
     * Get the expiry time of the given key.
     *
     * @param key The key to get the expiry time of
     * @return The expiry time of the given key
     */
    public @Nullable Instant getExpireAt(K key) {
        return this.expireAt.get(key);
    }

    /**
     * Get the duration until the given key expires.
     *
     * @param key The key to get the duration of
     * @return The duration until the given key expires
     */
    public @Nullable Long getDuration(K key) {
        Instant expireAt = getExpireAt(key);
        if (expireAt == null) {
            return null;
        }

        return expireAt.toEpochMilli() - Instant.now().toEpochMilli();
    }

    /**
     * Get the duration until the given key expires.
     *
     * @param key The key to get the duration of
     * @param timeUnit The time unit to return the duration in
     * @return The duration until the given key expires
     */
    public @Nullable Long getDuration(K key, TimeUnit timeUnit) {
        Long duration = getDuration(key);
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
    public V put(K key, V value) {
        addEntry(key);
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            addEntry(key);
        }

        super.putAll(m);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V previousKey = super.putIfAbsent(key, value);
        if (previousKey == null) {
            addEntry(key);
        }

        return previousKey;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V previousKey = super.compute(key, remappingFunction);
        if (previousKey == null) {
            addEntry(key);
        }

        return previousKey;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        V previousKey = super.computeIfAbsent(key, mappingFunction);
        if (previousKey == null) {
            addEntry(key);
        }

        return previousKey;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V previousKey = super.computeIfPresent(key, remappingFunction);
        if (previousKey == null) {
            addEntry(key);
        }

        return previousKey;
    }

    @Override
    public V remove(Object key) {
        ScheduledFuture<?> scheduledRemoval = this.scheduledRemovals.remove(key);
        if (scheduledRemoval != null) {
            scheduledRemoval.cancel(false);
        }

        this.expireAt.remove(key);
        return super.remove(key);
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
