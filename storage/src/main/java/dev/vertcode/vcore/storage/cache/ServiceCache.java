package dev.vertcode.vcore.storage.cache;

import dev.vertcode.vcore.storage.StorageObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServiceCache<I, V extends StorageObject<I>> {

    private final Map<I, V> cacheValues = new HashMap<>();
    private final Map<I, Instant> cacheTimes = new HashMap<>();

    private final Long cacheTime;
    private final TimeUnit cacheTimeUnit;

    public ServiceCache() {
        this(null, null);
    }

    public ServiceCache(Long cacheTime, TimeUnit cacheTimeUnit) {
        this.cacheTime = cacheTime;
        this.cacheTimeUnit = cacheTimeUnit;
    }

    /**
     * Add a value to the cache.
     *
     * @param value The value you want to add
     */
    public void add(V value) {
        I identifier = value.getIdentifier();

        this.cacheValues.put(identifier, value);
        this.cacheTimes.put(identifier, Instant.now());
    }

    /**
     * Remove a value from the cache.
     *
     * @param identifier The identifier of the value you want to remove
     */
    public void remove(I identifier) {
        this.cacheValues.remove(identifier);
        this.cacheTimes.remove(identifier);
    }

    /**
     * Check if the cache contains a value.
     *
     * @param identifier The identifier of the value you want to check
     * @return If the cache contains the value
     */
    public boolean contains(I identifier) {
        return this.cacheValues.containsKey(identifier);
    }

    /**
     * Get a cached value.
     *
     * @param identifier The identifier of the value you want to get
     * @return The cached value
     */
    public V getCachedValue(I identifier) {
        return this.cacheValues.get(identifier);
    }

    /**
     * Get all the cached values.
     *
     * @return All the cached values
     */
    public Collection<V> getCachedValues() {
        return this.cacheValues.values();
    }

    /**
     * The cache clean task.
     */
    public void clean() {
        // If the cache time is null, we don't need to clean the cache
        if (this.cacheTime == null || this.cacheTimeUnit == null) {
            return;
        }

        // Loop through all the cache values
        for (I identifier : new ArrayList<>(this.cacheValues.keySet())) {
            Instant cacheTime = this.cacheTimes.get(identifier);
            // If the cache time is null, we don't need to clean the cache
            if (cacheTime == null) {
                continue;
            }

            Instant expireAt = cacheTime.plusMillis(this.cacheTimeUnit.toMillis(this.cacheTime));
            // If the expireAt time is not before the current time, we don't need to clean the cache
            if (expireAt.isAfter(Instant.now())) {
                continue;
            }

            // Remove the cache value
            remove(identifier);
        }
    }

    @Override
    public String toString() {
        return "ServiceCache{" +
                "cacheValues=" + cacheValues +
                ", cacheTimes=" + cacheTimes +
                ", cacheTime=" + cacheTime +
                ", cacheTimeUnit=" + cacheTimeUnit +
                '}';
    }
}
