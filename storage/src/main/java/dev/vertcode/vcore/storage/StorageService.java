package dev.vertcode.vcore.storage;

import dev.vertcode.vcore.scheduler.Schedulers;
import dev.vertcode.vcore.scheduler.schedule.ScheduledTask;
import dev.vertcode.vcore.storage.annotation.StorageContext;
import dev.vertcode.vcore.storage.cache.ServiceCache;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public abstract class StorageService<I, V extends StorageObject<I>> {

    protected final Class<V> storageObjectClass;
    protected final ServiceCache<I, V> cache;
    private final ScheduledTask tickTask;

    private boolean autoSave = true;
    private long autoSaveInterval = 15L;
    private TimeUnit autoSaveIntervalUnit = TimeUnit.MINUTES;
    private Instant lastAutoSave = Instant.now();

    public StorageService(Class<V> storageObjectClass) {
        this(storageObjectClass, null, null);
    }

    public StorageService(Class<V> storageObjectClass, Long cacheTime, TimeUnit cacheTimeUnit) {
        this.storageObjectClass = storageObjectClass;
        this.cache = new ServiceCache<>(cacheTime, cacheTimeUnit);
        this.tickTask = Schedulers.BACKEND.run(this::tick, 1L, 1L, TimeUnit.SECONDS);
    }

    /**
     * Start the {@link StorageService}.
     */
    public abstract void startup();

    /**
     * Stop the {@link StorageService}.
     */
    public void shutdown() {
        // Save all the data
        saveAll();
        // Cancel the tick task
        this.tickTask.cancel();
    }

    /**
     * Tick the {@link StorageService}.
     */
    public void tick() {
        // Run the cache cleanup
        this.cache.clean();

        // If the auto save is disabled, return
        if (!this.autoSave) {
            return;
        }

        Instant nextAutoSave = this.lastAutoSave.plusMillis(this.autoSaveIntervalUnit.toMillis(this.autoSaveInterval));
        if (nextAutoSave.isAfter(Instant.now())) {
            return;
        }

        // Save all the data
        saveAll();
        // Update the last auto save
        this.lastAutoSave = Instant.now();
    }

    /**
     * Set the auto save interval.
     *
     * @param autoSaveInterval The auto save interval
     * @param autoSaveIntervalUnit The {@link TimeUnit} of the auto save interval
     */
    public void setUpdateInterval(long autoSaveInterval, TimeUnit autoSaveIntervalUnit) {
        this.autoSaveInterval = autoSaveInterval;
        this.autoSaveIntervalUnit = autoSaveIntervalUnit;
    }

    /**
     * Set if the {@link StorageService} should auto save.
     *
     * @param autoSave If the {@link StorageService} should auto save
     */
    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    /**
     * Get a {@link StorageObject} from the database.
     *
     * @param identifier The identifier of the value you want to get
     * @param cache      If the value should be cached
     * @return The value you want to get
     */
    public abstract @Nullable V get(I identifier, boolean cache);

    /**
     * Get a {@link StorageObject} from the database.
     *
     * @param identifier The identifier of the value you want to get
     * @return The value you want to get
     */
    public @Nullable V get(I identifier) {
        return this.get(identifier, true);
    }

    /**
     * Get all {@link StorageObject}s from the database.
     *
     * @param cache If the values should be cached
     * @return All the values
     */
    public abstract Collection<V> getAll(boolean cache);

    /**
     * Get all {@link StorageObject}s from the database.
     *
     * @return All the values
     */
    public Collection<V> getAll() {
        return this.getAll(false);
    }

    /**
     * Get a {@link StorageObject} from the cache.
     *
     * @param identifier The identifier of the value you want to get
     * @return The value you want to get
     */
    public @Nullable V getCached(I identifier) {
        return this.cache.getCachedValue(identifier);
    }

    /**
     * Add a {@link StorageObject} to the cache.
     *
     * @param value The value you want to add
     */
    public void addToCache(V value) {
        this.cache.add(value);
    }

    /**
     * Remove a {@link StorageObject} from the cache.
     *
     * @param identifier The identifier of the value you want to remove
     */
    public void removeFromCache(I identifier) {
        this.cache.remove(identifier);
    }

    /**
     * Check if the cache contains a {@link StorageObject}.
     *
     * @param identifier The identifier of the value you want to check
     * @return If the cache contains the value
     */
    public boolean contains(I identifier) {
        return this.cache.contains(identifier);
    }

    /**
     * Save a {@link StorageObject} to the database.
     *
     * @param value The value you want to save
     */
    public abstract void save(V value);

    /**
     * Save a {@link StorageObject} to the database asynchronously.
     *
     * @param value The value you want to save
     */
    public void saveAsync(V value) {
        Schedulers.BACKEND.run(() -> save(value));
    }

    /**
     * Save all cached {@link StorageObject}s to the database.
     */
    public void saveAll() {
        for (V value : this.cache.getCachedValues()) {
            save(value);
        }
    }

    /**
     * Delete a {@link StorageObject} from the database.
     *
     * @param value The value you want to delete
     */
    public abstract void delete(V value);

    /**
     * Delete a {@link StorageObject} from the database asynchronously.
     *
     * @param value The value you want to delete
     */
    public void deleteAsync(V value) {
        Schedulers.BACKEND.run(() -> delete(value));
    }

    /**
     * Get the {@link ServiceCache} of the {@link StorageService}.
     *
     * @return The {@link ServiceCache}
     */
    public ServiceCache<I, V> getCache() {
        return this.cache;
    }

    /**
     * Get the {@link Class} of the {@link StorageObject}.
     *
     * @return The {@link Class}
     */
    public Class<V> getStorageObjectClass() {
        return this.storageObjectClass;
    }

    /**
     * Get the {@link StorageContext} of the {@link StorageObject}.
     *
     * @return The {@link StorageContext}
     */
    protected @Nullable StorageContext getStorageContext() {
        try {
            return this.storageObjectClass.getAnnotation(StorageContext.class);
        }catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "StorageService{" +
                "storageObjectClass=" + storageObjectClass +
                ", cache=" + cache +
                '}';
    }
}
